package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.AccountProfileRepository;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.business.dto.AddAccountProfileViewModel;
import almroth.kim.gamendo_user_api.business.dto.RemoveViewModel;
import almroth.kim.gamendo_user_api.business.model.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository repository;

    private final AccountProfileRepository accountProfileRepository;

    public Business Get(String name){
        return repository.findBusinessByName(name).orElseThrow(() -> new IllegalArgumentException("No such Business"));
    }

    public void Create(Business business) {
        if (repository.existsBusinessByName(business.getName()))
            throw new IllegalArgumentException("Business with name already exists");

        repository.save(business);
    }
    public void Create(String name) {
        if (repository.existsBusinessByName(name))
            return;

        var business = Business.builder().name(name).build();

        repository.save(business);
    }

    public void Update(Business businessViewModel, Account account) {
        var business = repository.findBusinessByName(businessViewModel.getName()).orElseThrow(() -> new IllegalArgumentException("No such Business"));
        var accountProfiles = accountProfileRepository.findByAccount(account).orElseThrow(() -> new IllegalArgumentException("Error getting account profiles"));
        if (businessViewModel.getAccountProfiles() == null
                || businessViewModel.getName() == null)
            throw new IllegalArgumentException("All values in business are required");
        business.setName(businessViewModel.getName());
        business.setAccountProfiles(businessViewModel.getAccountProfiles());
        repository.save(business);
    }

    public void Delete(RemoveViewModel businessViewModel) {
        var business = repository.findBusinessByName(businessViewModel.getName()).orElseThrow(() -> new IllegalArgumentException("No such Business"));
        repository.delete(business);
    }

    public void AddAccountProfile(AddAccountProfileViewModel viewModel) {
        var business = repository.findBusinessByName(viewModel.getBusinessName()).orElseThrow(() -> new IllegalArgumentException("No such Business"));
        Set<AccountProfile> profiles = new HashSet<>();
        viewModel.getAccountEmail().forEach(email ->
                profiles.add(accountProfileRepository
                        .findByAccountEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("No account profile with email: " + email))));
        profiles.addAll(business.getAccountProfiles());
        business.setAccountProfiles(profiles);
    }

}
