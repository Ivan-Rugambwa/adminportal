package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    //    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Optional<Account> findByEmail(String email);
    Optional<Set<Account>> findAllByRoles(RoleType role);
}
