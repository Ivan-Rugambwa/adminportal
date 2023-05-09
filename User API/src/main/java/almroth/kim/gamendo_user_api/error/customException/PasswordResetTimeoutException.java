package almroth.kim.gamendo_user_api.error.customException;

public class PasswordResetTimeoutException extends RuntimeException {

    public PasswordResetTimeoutException(String errorMessage) {
        super(errorMessage);
    }
}
