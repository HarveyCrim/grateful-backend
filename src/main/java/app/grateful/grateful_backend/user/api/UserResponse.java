package app.grateful.grateful_backend.user.api;

import app.grateful.grateful_backend.user.domain.User;

import java.util.UUID;

public record UserResponse(UUID id,String email, String displayName, boolean emailVerified) {
    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.isEmailVerified()
        );
    }
}
