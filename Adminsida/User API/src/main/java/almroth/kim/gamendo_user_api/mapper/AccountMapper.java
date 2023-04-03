package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.data.SimpleResponse;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.refreshToken.model.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "refreshToken", ignore = true)
    Account ACCOUNT(SimpleResponse simpleResponse);

    @Mapping(target = "refreshToken", source = "refreshToken", qualifiedByName = "refreshTokenToString")
    SimpleResponse SIMPLE_RESPONSE(Account account);

    @Named("refreshTokenToString")
    default String refreshTokenToString(RefreshToken token) {
        return token.getToken();
    }
}
