package almroth.kim.seat_api.passwordReset;

import almroth.kim.seat_api.passwordReset.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {

    Optional<PasswordReset> findByAccount_Uuid(UUID uuid);

    Optional<PasswordReset> getByUuid(UUID uuid);

}
