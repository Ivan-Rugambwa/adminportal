package almroth.kim.seat_api.preRegister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdatePreRegisterRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String businessName;
    private String roleName;
}
