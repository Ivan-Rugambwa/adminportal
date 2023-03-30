package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.data.SimpleResponse;
import almroth.kim.gamendo_user_api.account.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

//    @Mapping(target = "refreshTokens", ignore = true)
//    Account ACCOUNT(SimpleResponse simpleResponse);

    SimpleResponse SIMPLE_RESPONSE(Account account);

//    @Named("refreshTokenToString")
//    default String refreshTokenToString(RefreshToken token) {
//        return token.getToken();
//    }
}
