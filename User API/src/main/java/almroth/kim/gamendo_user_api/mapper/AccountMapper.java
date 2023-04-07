package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    @Mapping(target = "businessName", source = "profile", qualifiedByName = "getBusinessName")
    @Mapping(target = "businessUuid", source = "profile", qualifiedByName = "getBusinessUuid")
    SimpleResponse SIMPLE_RESPONSE(Account account);

    @Named("getBusinessName")
    default String getBusinessName(AccountProfile profile) {return profile.getBusiness().getName();}

    @Named("getBusinessUuid")
    default UUID getBusinessUuid(AccountProfile profile) {return profile.getBusiness().getUuid();}

}
