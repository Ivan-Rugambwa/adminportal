package almroth.kim.seat_api.error.customException;

public class NoSuchTokenException extends RuntimeException {
    public NoSuchTokenException(String message) {
        super(message);
    }
}
