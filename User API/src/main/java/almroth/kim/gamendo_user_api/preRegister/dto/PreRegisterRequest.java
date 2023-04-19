package almroth.kim.gamendo_user_api.preRegister.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PreRegisterRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Email is required")
    private String firstName;
    @NotBlank(message = "Email is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    private String businessName;
}
