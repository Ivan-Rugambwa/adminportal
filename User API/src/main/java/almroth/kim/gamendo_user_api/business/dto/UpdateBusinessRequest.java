package almroth.kim.gamendo_user_api.business.dto;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
public class UpdateBusinessRequest {

    private String name;
    private Integer seatBaseline;
    private String accountUUID;
    @Pattern(regexp = "^(MONTHLY|QUARTERLY|SEMI|ANNUALLY)$", message = "Need to be MONTHLY, QUARTERLY, SEMI or ANNUALLY")
    private String emailFrequency;

}
