package almroth.kim.gamendo_user_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Wrong email format, see correct example: test@domain.com")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @NotBlank(message = "Business name is required")
    private String business;
}
