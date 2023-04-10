package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateSeatRequest {

    private Integer usedSeat;
    private String updatedByEmail;
    @Pattern(regexp = "\\b(FILL|REVIEW|COMPLETE)\\b", message = "Need to be FILL, REVIEW or COMPLETE")
    private String status;
}
