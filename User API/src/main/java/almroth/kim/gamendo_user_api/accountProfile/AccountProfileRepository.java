package almroth.kim.gamendo_user_api.accountProfile;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountProfileRepository extends JpaRepository<AccountProfile, UUID> {

    Optional<AccountProfile> findByAccount(Account account);
}
