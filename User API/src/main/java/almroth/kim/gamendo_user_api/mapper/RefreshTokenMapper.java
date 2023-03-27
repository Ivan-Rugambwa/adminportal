package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.refreshToken.dto.RefreshTokenResponse;
import almroth.kim.gamendo_user_api.refreshToken.model.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken REFRESH_TOKEN(RefreshTokenResponse refreshTokenResponse);

    @Mapping(target = "accountName", source = "account", qualifiedByName = "accountToAccountName")
    RefreshTokenResponse REFRESH_TOKEN_RESPONSE(RefreshToken refreshToken);

    @Named("accountToAccountName")
    default String accountToAccountName(Account account) {
        return account.getEmail();
    }
}
