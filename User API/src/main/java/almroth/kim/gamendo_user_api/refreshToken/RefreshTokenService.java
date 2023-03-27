package almroth.kim.gamendo_user_api.refreshToken;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.error.customException.NoSuchTokenException;
import almroth.kim.gamendo_user_api.error.customException.RefreshTokenException;
import almroth.kim.gamendo_user_api.mapper.RefreshTokenMapper;
import almroth.kim.gamendo_user_api.refreshToken.model.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public RefreshToken createRefreshToken(UUID accountUUID) {
        var account = accountRepository.findById(accountUUID).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));
        var refreshToken = RefreshToken
                .builder()
                .expirationDateInMilliSeconds(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(90)))
                .account(account)
                .token(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken createRefreshToken(Account account) {
        var refreshToken = RefreshToken
                .builder()
                .expirationDateInMilliSeconds(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(30)))
                .account(account)
                .token(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(RefreshToken token) {

        if (token.getExpirationDateInMilliSeconds().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token has expired. Please log in again.");
        }
        return createRefreshToken(token.getAccount());
    }

    @Transactional
    public int deleteRefreshTokenByUserId(UUID accountId) {
        return refreshTokenRepository.deleteByAccount(accountRepository.findById(accountId).get());
    }

}
