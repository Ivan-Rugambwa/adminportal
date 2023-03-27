package almroth.kim.gamendo_user_api.error.customException;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String errorMessage) {
        super(errorMessage);
    }
}
