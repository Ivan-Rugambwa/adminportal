package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.dto.UpdateAccountRequest;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.BusinessRepository;
import almroth.kim.gamendo_user_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.gamendo_user_api.mapper.AccountMapper;
import almroth.kim.gamendo_user_api.role.RoleRepository;
import almroth.kim.gamendo_user_api.role.RoleType;
import almroth.kim.gamendo_user_api.role.model.Role;
import com.password4j.BcryptFunction;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);
    BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.Y, 11);

    @Autowired
    public AccountService(AccountRepository accountRepository, BusinessRepository businessRepository, RoleRepository roleRepository) {

        this.accountRepository = accountRepository;
        this.businessRepository = businessRepository;
        this.roleRepository = roleRepository;
    }

    public List<SimpleResponse> getAccounts() {
        var accounts = accountRepository.findAll();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : accounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        return simpleData;
    }

    public List<SimpleResponse> getUserAccounts(){
        var accounts = accountRepository.findAll();
        var userAccounts = accounts.stream().filter(account -> account.getRoles().stream().allMatch(role -> role.getName() == RoleType.USER)).toList();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : userAccounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        return simpleData;
    }


    public void removeAccountByUUID(UUID uuid) {
        if (!accountRepository.existsById(uuid)) {
            throw new IllegalStateException("No account with uuid: " + uuid);
        }
        accountRepository.deleteById(uuid);
    }

    public Account getAccountByUuid(String uuid) {
        var account = accountRepository.findById(UUID.fromString(uuid));
        if (account.isEmpty()) {
            throw new IllegalStateException("No account with id: " + uuid);
        }
        return account.get();
    }

    public void updateAccount(String accountId, UpdateAccountRequest request) {
        Account account = accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + accountId));
        if (!(Objects.equals(account.getEmail(), request.getEmail()))) account.setEmail(request.getEmail());
        else if (accountRepository.existsByEmail(request.getEmail())) throw new EmailAlreadyTakenException("A user already has that email.");

        var business = businessRepository.findBusinessByName(request.getBusiness()).orElseThrow(() -> new IllegalArgumentException("No such business"));


        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.getProfile().setBusiness(business);

        accountRepository.save(account);
    }

    private String generateHashedPassword(String password) {
        return Password.hash(password).with(bcrypt).getResult();
    }

}
