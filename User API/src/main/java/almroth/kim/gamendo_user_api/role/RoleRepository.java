package almroth.kim.gamendo_user_api.role;

import almroth.kim.gamendo_user_api.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(RoleType name);
}
