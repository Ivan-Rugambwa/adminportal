package almroth.kim.gamendo_user_api.auth.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String message;
    private String username;
    private String token;
}
