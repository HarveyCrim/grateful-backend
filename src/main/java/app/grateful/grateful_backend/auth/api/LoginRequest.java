package app.grateful.grateful_backend.auth.api;

import jakarta.persistence.Column;
import lombok.Getter;

public record LoginRequest(
        @Column(nullable = false)
        String email,

        @Column(nullable = false)
        String password
) {
}
