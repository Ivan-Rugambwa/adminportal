package almroth.kim.gamendo_user_api.role;

import almroth.kim.gamendo_user_api.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(RoleType name);
}
