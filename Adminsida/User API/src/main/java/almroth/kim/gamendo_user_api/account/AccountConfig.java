package almroth.kim.gamendo_user_api.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository) {
        return args -> {
//            Account acc1 = new Account("kim@alm.com", "test", "kim", "almroth");
//            Account acc2 = new Account("john@doe.com", "test", "john", "doe");
//            accountRepository.saveAll(List.of(acc1, acc2));
        };
    }
}
