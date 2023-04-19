package almroth.kim.gamendo_user_api.role;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.auth.AuthenticationService;
import almroth.kim.gamendo_user_api.auth.dto.RegisterRequest;
import almroth.kim.gamendo_user_api.business.BusinessRepository;
import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.role.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Configuration
@Order(1)
public class RoleConfig {
    @Bean
    CommandLineRunner roleConfigRunner(RoleRepository repository, AccountRepository accountRepository, AuthenticationService authenticationService, BusinessRepository businessRepository) {
        if (repository.count() > 0) return args -> {
        };
        return args -> {
            CreateBusiness(businessRepository);
            createRoles(repository);
            register(accountRepository, authenticationService);

        };
    }
    @Transactional
    public void createRoles(RoleRepository repository){
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
    @Transactional
    public void register(AccountRepository accountRepository, AuthenticationService authenticationService){
        if(accountRepository.count() > 0) return;

        var acc1 = RegisterRequest.builder().email("kim@test.com").password("testtest").firstName("Kim").lastName("Almroth").build();
        var acc2 = RegisterRequest.builder().email("john@doe.com").password("testtest").firstName("john").lastName("doe").business("ICA").build();
        var acc3 = RegisterRequest.builder().email("mary@lombok.com").password("testtest").firstName("mary").lastName("lombok").business("ICA").build();
        var acc4 = RegisterRequest.builder().email("jane@smith.com").password("testtest").firstName("jane").lastName("smith").business("MAX").build();
        var acc5 = RegisterRequest.builder().email("harry@stone.com").password("testtest").firstName("harry").lastName("stone").business("IKEA").build();

        authenticationService.register(acc1, true);
        authenticationService.register(acc2, false);
        authenticationService.register(acc3, false);
        authenticationService.register(acc4, false);
        authenticationService.register(acc5, false);
    }
    @Transactional
    public void CreateBusiness(BusinessRepository repository){
        var bus1 = Business.builder().seatBaseline(45).name("ICA").build();
        var bus3 = Business.builder().seatBaseline(47).name("MAX").build();
        var bus4 = Business.builder().seatBaseline(42).name("IKEA").build();

        repository.saveAll(Set.of(bus1, bus3, bus4));
    }
}