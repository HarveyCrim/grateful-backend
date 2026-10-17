package app.grateful.grateful_backend.auth.api;

import app.grateful.grateful_backend.user.api.UserResponse;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public record AuthResponse(
        @NonNull
        Map<String, String> jwt,

        @NonNull
        UserResponse userResponse

) {
}
