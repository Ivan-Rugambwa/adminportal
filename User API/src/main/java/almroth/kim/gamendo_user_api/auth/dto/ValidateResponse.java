package almroth.kim.gamendo_user_api.auth.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ValidateResponse {
    private String message;
    private HttpStatus status;
}
