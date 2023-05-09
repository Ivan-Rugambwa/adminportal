package almroth.kim.gamendo_user_api.error.customException;

public class BusinessNameAlreadyExistsException extends RuntimeException {

    public BusinessNameAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
