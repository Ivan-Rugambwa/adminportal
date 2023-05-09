package almroth.kim.gamendo_user_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishResetRequest {
    private UUID resetPasswordUuid;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String newPassword;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String confirmNewPassword;
}
