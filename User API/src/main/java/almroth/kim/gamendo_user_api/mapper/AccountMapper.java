package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.role.RoleType;
import almroth.kim.gamendo_user_api.role.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    @Mapping(target = "businessName", source = "profile", qualifiedByName = "getBusinessName")
    @Mapping(target = "businessUuid", source = "profile", qualifiedByName = "getBusinessUuid")
    @Mapping(target = "roleNames", source = "account", qualifiedByName = "getRoleNames")
    SimpleResponse SIMPLE_RESPONSE(Account account);

    @Named("getRoleNames")
    default Set<String> getRoleNames(Account account) {
        if (account.getRoles() == null) return null;
        return  account.getRoles()
                .stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toSet());
    }

    @Named("getBusinessName")
    default String getBusinessName(AccountProfile profile) {
        if (profile == null) return null;
        return profile.getBusiness().getName();
    }

    @Named("getBusinessUuid")
    default UUID getBusinessUuid(AccountProfile profile) {
        if (profile == null) return null;
        return profile.getBusiness().getUuid();
    }

}
