package app.grateful.grateful_backend.user.infra;

import app.grateful.grateful_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAppleSub(String appleSub);
    Optional<User> findByGoogleSub(String googleSub);
}
