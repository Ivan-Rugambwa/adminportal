package almroth.kim.seat_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;


@Data
public class RegisterWithPreRegisterRequest {
    @NotNull
    private UUID preRegisterUuid;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String password;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password needs to have at least 8 characters.")
    @Size(max = 64, message = "Password can not have more than 64 characters.")
    private String confirmPassword;
}
