package app.grateful.grateful_backend.auth.service;

import app.grateful.grateful_backend.auth.api.AuthResponse;
import app.grateful.grateful_backend.auth.api.LoginRequest;
import app.grateful.grateful_backend.auth.api.RegisterRequest;
import app.grateful.grateful_backend.auth.domain.AppUserDetails;
import app.grateful.grateful_backend.common.ApiException;
import app.grateful.grateful_backend.user.api.UserResponse;
import app.grateful.grateful_backend.user.domain.User;
import app.grateful.grateful_backend.user.infra.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.ZoneId;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(TokenService tokenService, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new ApiException(HttpStatus.CONFLICT, "EMAIL_ALREADY_TAKEN", "email already taken: "+request.email());
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName());
        user.setTimezone(validTimezone(request.timezone()));

        try{
            UserResponse userResponse = UserResponse.from(userRepository.save(user));
            return new AuthResponse(tokenService.createAccessToken(user.getId(), user.getEmail()), userResponse);
        }
        catch(DataIntegrityViolationException e){
            throw new ApiException(HttpStatus.CONFLICT, "EMAIL_ALREADY_TAKEN", "email already taken: "+request.email());
        }
    }

    public AuthResponse login(LoginRequest request){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password())
            );
            AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();
            UserResponse userResponse = new UserResponse(principal.getId(), principal.getEmail(), principal.getDisplayName(), principal.isEmailVerified());
            return new AuthResponse(tokenService.createAccessToken(userResponse.id(), userResponse.email()), userResponse);
        }
        catch(Exception e){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "invalid username or password");
        }
    }

    private String validTimezone(String timezone){
        try{
            return ZoneId.of(timezone).getId();
        }
        catch(DateTimeException e){
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_TIMEZONE", "Unknown timezone: "+ timezone);
        }
    }
}
