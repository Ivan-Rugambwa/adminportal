package almroth.kim.seat_api.role;

import almroth.kim.seat_api.role.model.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Data
public class RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getRoleByName(RoleType name) {
        return repository.findRoleByName(name).orElseThrow(() -> new UsernameNotFoundException("No such role"));
    }
}
