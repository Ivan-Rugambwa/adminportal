package almroth.kim.gamendo_user_api.business.dto;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UpdateBusinessRequest {

    private String name;

    private Set<AccountProfile> accountProfiles;
    private Integer seatAmount;
    private String accountUUID;
}
