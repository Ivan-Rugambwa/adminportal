package almroth.kim.gamendo_user_api.role;

import almroth.kim.gamendo_user_api.role.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleConfig {
    @Bean
    CommandLineRunner roleConfigRunner(RoleRepository repository) {
        if (repository.count() > 0) return args -> {
        };
        return args -> {
            Role role1 = Role.builder()
                    .name(RoleType.USER)
                    .description("End user")
                    .build();
            Role role2 = Role.builder()
                    .name(RoleType.ADMIN)
                    .description("Site administrator")
                    .build();
            repository.saveAll(List.of(role1, role2));
        };
    }
}