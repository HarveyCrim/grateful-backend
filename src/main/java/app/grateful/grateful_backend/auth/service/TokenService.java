package app.grateful.grateful_backend.auth.service;

import app.grateful.grateful_backend.auth.api.AuthResponse;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    public static final Long ACCESS_TTL = (long) 15 * 60 * 1000;

    public TokenService(JwtEncoder encoder){
        this.encoder = encoder;
    }

    public Map<String, String> createAccessToken(UUID userId, String email){
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ACCESS_TTL);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("grateful")
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return Map.of("token", token, "expiresAt", expiresAt.toString());
    }
}
