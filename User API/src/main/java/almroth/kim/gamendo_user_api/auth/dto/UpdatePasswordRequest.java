package almroth.kim.gamendo_user_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String oldPassword;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String newPassword;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String confirmNewPassword;
}
