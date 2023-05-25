package almroth.kim.seat_api.account;

import almroth.kim.seat_api.account.dto.SimpleResponse;
import almroth.kim.seat_api.account.dto.UpdateAccountRequest;
import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.business.BusinessRepository;
import almroth.kim.seat_api.business.BusinessService;
import almroth.kim.seat_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.seat_api.mapper.AccountMapper;
import almroth.kim.seat_api.role.RoleRepository;
import almroth.kim.seat_api.role.RoleType;
import com.password4j.BcryptFunction;
import com.password4j.types.Bcrypt;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BusinessService businessService;

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Autowired
    public AccountService(AccountRepository accountRepository, BusinessRepository businessRepository, RoleRepository roleRepository, @Lazy BusinessService businessService) {

        this.accountRepository = accountRepository;
        this.businessService = businessService;
    }

    public List<SimpleResponse> getAccounts() {
        var accounts = accountRepository.findAll();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : accounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        return simpleData;
    }

    public List<SimpleResponse> getUserAccounts() {
        System.out.println("Getting user accounts only...");
        var accounts = accountRepository.findAll();
        var userAccounts = accounts.stream().filter(account -> account.getRoles().stream().allMatch(role -> role.getName() == RoleType.USER)).toList();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : userAccounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        System.out.println("Successfully got user accounts only.");
        return simpleData;
    }

    public Set<SimpleResponse> getAccountsByBusiness(String businessName) {
        var accounts = accountRepository.findAllByProfile_Business_Name(businessName).orElse(new HashSet<>());
        var simpleAccounts = new HashSet<SimpleResponse>();
        for (var account :
                accounts) {
            simpleAccounts.add(mapper.SIMPLE_RESPONSE(account));
        }
        return simpleAccounts;
    }


    public void removeAccountByUUID(UUID uuid) {
        if (!accountRepository.existsById(uuid)) {
            return;
        }
        accountRepository.deleteById(uuid);
    }

    public SimpleResponse getSimpleAccountByUuid(String uuid) {
        Account account = accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + uuid));
        return mapper.SIMPLE_RESPONSE(account);
    }

    public Account getAccountByUuid(String uuid) {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + uuid));
    }

    public SimpleResponse updateAccount(UUID accountId, UpdateAccountRequest request) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new IllegalStateException("No account with id: " + accountId));


        if (!request.getEmail().isBlank()) {
            if (!Objects.equals(account.getEmail(), request.getEmail()) && accountRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyTakenException("That email is unavailable.");
            } else {
                account.setEmail(request.getEmail());
            }
        }

        if (!request.getFirstName().isBlank())
            account.setFirstName(StringUtils.capitalize(request.getFirstName()));

        if (!request.getLastName().isBlank())
            account.setLastName(StringUtils.capitalize(request.getLastName()));

        if (!request.getBusiness().isBlank() && account.getRoles().stream().noneMatch(role -> role.getName() == RoleType.ADMIN)) {
            var business = businessService.GetByName(request.getBusiness());
            account.getProfile().setBusiness(business);
        }

        accountRepository.saveAndFlush(account);
        return mapper.SIMPLE_RESPONSE(account);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No account with that email"));
    }

    public boolean doesAccountExistByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
