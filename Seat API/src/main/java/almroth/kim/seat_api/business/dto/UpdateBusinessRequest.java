package almroth.kim.seat_api.business.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateBusinessRequest {

    private String name;
    private Integer seatBaseline;
    private String accountUUID;
    @Pattern(regexp = "^(MONTHLY|QUARTERLY|SEMI|ANNUALLY)$", message = "Need to be MONTHLY, QUARTERLY, SEMI or ANNUALLY")
    private String emailFrequency;

}
