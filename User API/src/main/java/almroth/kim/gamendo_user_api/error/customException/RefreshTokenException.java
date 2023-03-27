package almroth.kim.gamendo_user_api.error.customException;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
