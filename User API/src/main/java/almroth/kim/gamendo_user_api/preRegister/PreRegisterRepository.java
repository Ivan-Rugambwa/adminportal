package almroth.kim.gamendo_user_api.preRegister;

import almroth.kim.gamendo_user_api.preRegister.model.PreRegister;
import almroth.kim.gamendo_user_api.seat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PreRegisterRepository extends JpaRepository<PreRegister, UUID> {

    Optional<PreRegister> findByEmail(String email);
}
