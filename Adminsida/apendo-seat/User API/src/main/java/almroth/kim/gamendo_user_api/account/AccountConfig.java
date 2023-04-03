package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.auth.AuthenticationService;
import almroth.kim.gamendo_user_api.auth.dto.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Order(2)
public class AccountConfig {

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository,AuthenticationService authenticationService) {
        return args -> {

        };
    }
}
