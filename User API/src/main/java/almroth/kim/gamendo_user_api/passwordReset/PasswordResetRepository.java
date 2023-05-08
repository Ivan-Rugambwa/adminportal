package almroth.kim.gamendo_user_api.passwordReset;

import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.passwordReset.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {

    boolean existsByAccount_Uuid(UUID uuid);

    Optional<PasswordReset> findByAccount_Uuid(UUID uuid);

    Optional<PasswordReset> getByUuid(UUID uuid);

}
