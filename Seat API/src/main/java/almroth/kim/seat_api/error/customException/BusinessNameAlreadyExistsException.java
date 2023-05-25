package almroth.kim.seat_api.error.customException;

public class BusinessNameAlreadyExistsException extends RuntimeException {

    public BusinessNameAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
