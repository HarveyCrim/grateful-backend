package app.grateful.grateful_backend.auth.service;

import app.grateful.grateful_backend.auth.domain.RefreshToken;
import app.grateful.grateful_backend.auth.infra.RefreshTokenRepository;
import app.grateful.grateful_backend.common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final SecureRandom random = new SecureRandom();
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long REFRESH_TTL = 30L;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String create(UUID userId){
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(hash(raw));
        refreshToken.setExpiresAt(Instant.now().plusSeconds(REFRESH_TTL * 24 * 60 * 60));
        refreshTokenRepository.save(refreshToken);
        return raw;
    }

    @Transactional(noRollbackFor = ApiException.class)
    public UUID consume(String raw){
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash(raw))
                .orElseThrow(RefreshTokenService::invalid);
        if(token.getRevokedAt() != null){
            refreshTokenRepository.revokeAllForUser(token.getUserId(), Instant.now());
            throw invalid();
        }
        if(token.getExpiresAt().isBefore(Instant.now())){
            throw  invalid();
        }
        token.setRevokedAt(Instant.now());
        return token.getUserId();
    }

    @Transactional
    public void revoke(String raw){
        refreshTokenRepository.findByTokenHash(hash(raw)).ifPresent(
                token -> {
                    if(token.getRevokedAt() == null){
                        token.setRevokedAt(Instant.now());
                    }
                }
        );
    }

    private static ApiException invalid(){
        return new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "please log in again");
    }

    private static String hash(String raw){
        try {
            byte[] digest = 
        }
    }
}
