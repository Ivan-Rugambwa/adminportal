package almroth.kim.seat_api.preRegister;

import almroth.kim.seat_api.preRegister.model.PreRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreRegisterRepository extends JpaRepository<PreRegister, UUID> {

    Optional<PreRegister> findByEmail(String email);
}
