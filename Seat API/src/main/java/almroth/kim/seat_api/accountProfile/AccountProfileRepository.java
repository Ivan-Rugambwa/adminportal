package almroth.kim.seat_api.accountProfile;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.accountProfile.model.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountProfileRepository extends JpaRepository<AccountProfile, UUID> {

    Optional<AccountProfile> findByAccount(Account account);

    Optional<AccountProfile> findByAccountEmail(String email);

    boolean existsByAccount_EmailAndBusiness_Name(String email, String name);
}
