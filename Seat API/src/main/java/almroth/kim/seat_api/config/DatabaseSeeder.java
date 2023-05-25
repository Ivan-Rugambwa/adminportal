package almroth.kim.seat_api.config;

import almroth.kim.seat_api.account.AccountRepository;
import almroth.kim.seat_api.auth.AuthenticationService;
import almroth.kim.seat_api.auth.dto.RegisterRequest;
import almroth.kim.seat_api.business.BusinessRepository;
import almroth.kim.seat_api.business.model.Business;
import almroth.kim.seat_api.preRegister.PreRegisterService;
import almroth.kim.seat_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.seat_api.role.RoleRepository;
import almroth.kim.seat_api.role.RoleType;
import almroth.kim.seat_api.role.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final NotionConfigProperties env;

    @Bean
    CommandLineRunner DatabaseSeederRunner(RoleRepository repository,
                                           PreRegisterService preRegisterService) {
        if (repository.count() > 0) return args -> {
        };
        return args -> {
            createRoles(repository);
            createFirstAdmin(preRegisterService);
        };
    }

    private void createFirstAdmin(PreRegisterService preRegisterService) {
        var acc = PreRegisterRequest.builder().email(env.adminEmail()).firstName(env.adminFirstName()).lastName(env.adminLastName()).roleName("ADMIN").build();
        preRegisterService.Create(acc);
    }

    @Transactional
    public void createRoles(RoleRepository repository) {
        if (repository.count() > 0) return;

        Role role1 = Role.builder()
                .name(RoleType.USER)
                .description("End user")
                .build();
        Role role2 = Role.builder()
                .name(RoleType.ADMIN)
                .description("Site administrator")
                .build();
        repository.saveAll(List.of(role1, role2));
    }

}