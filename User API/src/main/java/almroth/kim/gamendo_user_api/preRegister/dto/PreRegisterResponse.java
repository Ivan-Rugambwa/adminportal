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
public class PreRegisterResponse {
    private UUID uuid;
    private String email;
    private String firstName;
    private String lastName;
    private String businessName;
    private String roleName;
}
