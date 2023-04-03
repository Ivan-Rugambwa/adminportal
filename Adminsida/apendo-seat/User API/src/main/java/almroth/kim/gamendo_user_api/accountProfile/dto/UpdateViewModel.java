package almroth.kim.gamendo_user_api.accountProfile.dto;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateViewModel {
    private String accountEmail;
    private String businessName;
}
