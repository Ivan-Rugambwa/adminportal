package almroth.kim.gamendo_user_api.role;

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

    public Role getRoleById(int id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("No such role"));
    }

    public Role getRoleByName(RoleType name) {
        return repository.findRoleByName(name).orElseThrow(() -> new UsernameNotFoundException("No such role"));
    }
}
