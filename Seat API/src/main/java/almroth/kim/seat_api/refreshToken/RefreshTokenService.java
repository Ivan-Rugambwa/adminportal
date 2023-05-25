package almroth.kim.seat_api.refreshToken;

import almroth.kim.seat_api.account.AccountRepository;
import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.error.customException.NoSuchTokenException;
import almroth.kim.seat_api.error.customException.RefreshTokenException;
import almroth.kim.seat_api.mapper.RefreshTokenMapper;
import almroth.kim.seat_api.refreshToken.model.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Data

public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;
    private final RefreshTokenMapper mapper = Mappers.getMapper(RefreshTokenMapper.class);

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, AccountRepository accountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountRepository = accountRepository;
    }

    public RefreshToken GetRefreshTokenByToken(String token) {
        return refreshTokenRepository.findRefreshTokenByToken(token).orElseThrow(() -> new NoSuchTokenException("Invalid token: " + token));
    }

    public RefreshToken createRefreshToken(Account account) {

        expireAllRefreshTokens(account);
        var tokens = account.getRefreshTokens();
        var expireDate = (tokens == null || tokens.isEmpty()) ?
                Instant.now().plusMillis(TimeUnit.DAYS.toMillis(100)) :
                account.getRefreshTokens().iterator().next().getExpirationDateInMilliSeconds();

        var refreshToken = RefreshToken
                .builder()
                .expirationDateInMilliSeconds(expireDate)
                .account(account)
                .isExpiredByNewToken(false)
                .token(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void verifyRefreshToken(RefreshToken token) {

        if (token.isExpiredByNewToken()) {
            deleteAllRefreshTokens(token.getAccount());
            throw new RefreshTokenException("Refresh token has expired. DELETE Please log in again.");
        }

        if (token.getExpirationDateInMilliSeconds().compareTo(Instant.now()) < 0) {
            expireAllRefreshTokens(token.getAccount());
            throw new RefreshTokenException("Refresh token has expired. Please log in again.");
        }
    }

    private void expireAllRefreshTokens(Account account) {
        var refreshToken = refreshTokenRepository.findRefreshTokenByAccountAndIsExpiredByNewTokenIsFalse(account);
        if (refreshToken.isEmpty()) return;
        refreshToken.get().setExpiredByNewToken(true);
        refreshTokenRepository.save(refreshToken.get());
    }

    @Transactional
    public void deleteAllRefreshTokens(Account account) {
        account.getRefreshTokens().clear();
        accountRepository.save(account);
    }

}
