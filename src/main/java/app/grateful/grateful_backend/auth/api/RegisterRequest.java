package app.grateful.grateful_backend.auth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 9, max = 72) String password,
        @Size(max = 50) String displayName,
        @NotBlank String timezone
) {
}
