package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.account.AccountService;
import almroth.kim.gamendo_user_api.accountProfile.AccountProfileRepository;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.business.dto.*;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.error.customException.BusinessNameAlreadyExistsException;
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

    public Business GetByName(String name) {
        return repository.findBusinessByName(name).orElseThrow(() -> new IllegalArgumentException("No such Business: " + name));
    }

    public Business GetByUuid(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No Business with id: " + uuid));
    }

    public BusinessResponse GetResponseByUuid(UUID uuid) {
        var business = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No Business with id: " + uuid));
        return mapper.TO_RESPONSE(business);
    }

    public BusinessResponse Create(CreateBusinessRequest model) {
        if (repository.existsBusinessByName(model.getName()))
            throw new BusinessNameAlreadyExistsException("Business with name already exists");

        model.setName(model.getName().toUpperCase());

        var business = repository.save(mapper.TO_MODEL(model));

        return mapper.TO_RESPONSE(business);
    }

    public BusinessResponse Update(UpdateBusinessRequest request, UUID uuid) {
        var business = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such Business"));
        System.out.println("Updating business....");

        if (request.getAccountUUID() != null) {
            var account = accountService.getAccountByUuid(request.getAccountUUID());
            business.getAccountProfiles().add(account.getProfile());
        }
        if (request.getSeatBaseline() != null) {
            business.setSeatBaseline(request.getSeatBaseline());
        }
        if (request.getName() != null && !business.getName().equals(request.getName())) {
            if (repository.existsBusinessByName(request.getName()))
                throw new IllegalArgumentException("Business with that name already exists");

            business.setName(request.getName());
        }
        if (request.getEmailFrequency() != null) {
            business.setEmailFrequency(request.getEmailFrequency());
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
