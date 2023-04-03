package almroth.kim.gamendo_user_api.account.data;

import lombok.Getter;

public class LoginRequest {

    @Getter
    private String email;
    @Getter
    private String password;
}
