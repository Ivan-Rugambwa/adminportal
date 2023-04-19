package almroth.kim.gamendo_user_api.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateAccountRequest {
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Wrong email format, see correct example: test@domain.com")
    private String email;
    private String firstName;
    private String lastName;
    private String business;
}
