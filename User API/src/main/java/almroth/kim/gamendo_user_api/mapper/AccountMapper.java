package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    @Mapping(target = "businessName", source = "profile", qualifiedByName = "getBusinessName")
    SimpleResponse SIMPLE_RESPONSE(Account account);

    @Named("getBusinessName")
    default String getBusinessName(AccountProfile profile) {return profile.getBusiness().getName();}

}
