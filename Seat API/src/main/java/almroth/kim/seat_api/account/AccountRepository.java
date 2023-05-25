package almroth.kim.seat_api.account;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<Set<Account>> findAllByProfile_Business_Name(String name);
}
