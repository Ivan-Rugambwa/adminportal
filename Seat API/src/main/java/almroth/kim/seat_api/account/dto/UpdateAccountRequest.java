package almroth.kim.seat_api.account.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAccountRequest {
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Wrong email format, see correct example: test@domain.com")
    private String email;
    private String firstName;
    private String lastName;
    private String business;
}
