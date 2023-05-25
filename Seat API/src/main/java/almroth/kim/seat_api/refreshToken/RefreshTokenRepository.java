package almroth.kim.seat_api.refreshToken;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.refreshToken.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    @SuppressWarnings("SpringDataMethodInconsistencyInspection")
    Optional<RefreshToken> findRefreshTokenByAccountAndIsExpiredByNewTokenIsFalse(Account account);
}
