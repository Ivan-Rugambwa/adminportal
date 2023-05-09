package almroth.kim.gamendo_user_api.error;

import almroth.kim.gamendo_user_api.error.customException.*;
import almroth.kim.gamendo_user_api.error.dto.BadCredentialsResponse;
import almroth.kim.gamendo_user_api.error.dto.ErrorResponse;
import almroth.kim.gamendo_user_api.error.dto.ValidationErrorResponse;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(PasswordResetTimeoutException.class)
    public ResponseEntity<ErrorResponse> PasswordResetTimeout(Exception ex, WebRequest req) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyTaken(Exception ex, WebRequest req) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_CONFLICT);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(BusinessNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> businessNameAlreadyTaken(Exception ex, WebRequest req) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_CONFLICT);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(DataBadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentials(DataBadCredentialsException ex) {
        BadCredentialsResponse error = new BadCredentialsResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        error.setMessage(ex.getMessage() + ": bad username or password");
        error.setUsername(ex.getUsername());
        error.setPassword(ex.getPassword());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(NoSuchTokenException.class)
    public ResponseEntity<ErrorResponse> noSuchRefreshToken(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> RefreshTokenException(RefreshTokenException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> ConstraintViolationException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setDate(LocalDateTime.now());
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        ArrayList<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        errorResponse.setErrors(errors);
        errorResponse.setMessage("Validation error.");

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());

        error.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        error.setMessage(ex.getLocalizedMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> Exception(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());

        error.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        error.setMessage(ex.getLocalizedMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> SignatureException(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setDate(LocalDateTime.now());

        error.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        error.setMessage(ex.getLocalizedMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
