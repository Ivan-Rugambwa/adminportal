package almroth.kim.seat_api.preRegister.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreRegisterRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;

    private String businessName;
    @NotBlank(message = "Role is required")
    private String roleName;
}
