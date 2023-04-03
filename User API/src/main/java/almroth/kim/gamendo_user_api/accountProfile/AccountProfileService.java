package almroth.kim.gamendo_user_api.accountProfile;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.dto.AddAccountViewModel;
import almroth.kim.gamendo_user_api.accountProfile.dto.CreateViewModel;
import almroth.kim.gamendo_user_api.accountProfile.dto.RemoveViewModel;
import almroth.kim.gamendo_user_api.accountProfile.dto.UpdateViewModel;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.business.BusinessRepository;
import almroth.kim.gamendo_user_api.business.dto.AddAccountProfileViewModel;
import almroth.kim.gamendo_user_api.business.model.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class AccountProfileService {

    private final AccountProfileRepository repository;
    private final BusinessRepository businessRepository;
    private final AccountRepository accountRepository;

    public void Create(CreateViewModel model) {
        var business = businessRepository.findBusinessByName(model.getBusinessName()).orElseThrow(() -> new IllegalArgumentException("No such business"));
        var account = accountRepository.findByEmail(model.getAccountEmail()).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));

        if (repository.existsByAccount_EmailAndBusiness_Name(model.getAccountEmail(), model.getBusinessName()))
            throw new IllegalArgumentException("Account Profile already exists");

        AccountProfile accountProfile = AccountProfile.builder()
                .account(account)
                .business(business)
                .build();

        repository.save(accountProfile);
    }
    public void Create(AccountProfile model) {
        var business = businessRepository.findById(model.getBusiness().getUuid()).orElseThrow(() -> new IllegalArgumentException("No such business"));
        var account = accountRepository.findById(model.getAccount().getUuid()).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));

        if (repository.existsByAccount_EmailAndBusiness_Name(model.getAccount().getEmail(), model.getBusiness().getName()))
            throw new IllegalArgumentException("Account Profile already exists");

        repository.save(model);
    }

    public void Update(UpdateViewModel model) {
        var accountProfile = repository.findByAccountEmail(model.getAccountEmail()).orElseThrow(() -> new IllegalArgumentException("No such account profile"));

        var business = businessRepository.findBusinessByName(model.getBusinessName()).orElseThrow(() -> new IllegalArgumentException("No such business"));
        var account = accountRepository.findByEmail(model.getAccountEmail()).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));

        if (accountProfile.getAccount() == null
                || accountProfile.getBusiness() == null)
            throw new IllegalArgumentException("All values in account profile are required");
        accountProfile.setAccount(account);
        accountProfile.setBusiness(business);
        repository.save(accountProfile);
    }

    public void Delete(RemoveViewModel model) {
        var accountProfile = repository.findByAccountEmail(model.getAccountEmail()).orElseThrow(() -> new IllegalArgumentException("No such account profile"));
        repository.delete(accountProfile);
    }

    public void AddAccount(AddAccountViewModel model) {
        var accountProfile = repository.findByAccountEmail(model.getAccountEmail()).orElseThrow(() -> new IllegalArgumentException("No such account profile"));
        var account = accountRepository.findByEmail(model.getAccountEmail()).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));
        accountProfile.getAccount().setProfile(accountProfile);
        repository.save(accountProfile);
    }
}
