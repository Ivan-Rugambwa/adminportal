package almroth.kim.seat_api.business;

import almroth.kim.seat_api.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {

    boolean existsBusinessByName(String name);

    Optional<Business> findBusinessByName(String name);

}
