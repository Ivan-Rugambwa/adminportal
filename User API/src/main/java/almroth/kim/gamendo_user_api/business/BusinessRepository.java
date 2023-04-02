package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {


}
