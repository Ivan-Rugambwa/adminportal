package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.account.AccountService;
import almroth.kim.gamendo_user_api.accountProfile.AccountProfileRepository;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.business.dto.*;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.mapper.BusinessMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository repository;
    private final AccountProfileRepository accountProfileRepository;
    private final AccountService accountService;
    private final BusinessMapper mapper = Mappers.getMapper(BusinessMapper.class);

    public Business GetByName(String name){
        return repository.findBusinessByName(name).orElseThrow(() -> new IllegalArgumentException("No such Business: " + name));
    }
    public Business GetByUuid(String uuid){
        return repository.findById(UUID.fromString(uuid)).orElseThrow(() -> new IllegalArgumentException("No Business with id: " + uuid));
    }

    public void Create(CreateBusinessRequest model) {
        if (repository.existsBusinessByName(model.getName()))
            throw new IllegalArgumentException("Business with name already exists");
        model.setName(model.getName().toUpperCase());
        repository.save(mapper.TO_MODEL(model));
    }

    public BusinessResponse Update(UpdateBusinessRequest request, UUID uuid) {
        var business = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such Business"));
        System.out.println("Updating business....");

        if (request.getAccountUUID() != null){
            var account = accountService.getAccountByUuid(request.getAccountUUID());
            business.getAccountProfiles().add(account.getProfile());
            System.out.println("Adding account");
        }
        if (request.getSeatAmount() != null){
            business.setSeatBaseline(request.getSeatAmount());
            System.out.println("Changing seat baseline");
        }
        if (request.getName() != null){
            business.setName(request.getName());
            System.out.println("Changing name");
        }

        repository.save(business);
        return mapper.TO_RESPONSE(business);
    }

    @Transactional
    public void Delete(UUID uuid) {
        var business = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such Business"));
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

    public List<BusinessResponse> GetAll() {
        System.out.println("Trying to get all businesses...");
        var businesses = repository.findAll();
        ArrayList<BusinessResponse> simpleData = new ArrayList<>();
        for (Business business : businesses) {
            simpleData.add(mapper.TO_RESPONSE(business));
        }
        System.out.println("Successfully got all businesses");
        return simpleData;
    }
}
