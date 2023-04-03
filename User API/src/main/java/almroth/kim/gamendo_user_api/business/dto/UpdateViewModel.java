package almroth.kim.gamendo_user_api.business.dto;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateViewModel {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Account profiles are required")

    private Set<AccountProfile> accountProfiles;
}
