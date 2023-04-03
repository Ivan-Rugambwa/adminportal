package almroth.kim.gamendo_user_api.refreshToken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private String token;
    private Instant expirationDateInMilliSeconds;
    private String accountName;
}
