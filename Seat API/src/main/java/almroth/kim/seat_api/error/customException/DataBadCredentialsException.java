package almroth.kim.seat_api.error.customException;

import lombok.Getter;

@Getter
public class DataBadCredentialsException extends RuntimeException {
    private final String username;
    private final String password;

    public DataBadCredentialsException(String message, String username, String password) {
        super(message);
        this.username = username;
        this.password = password;
    }
}
