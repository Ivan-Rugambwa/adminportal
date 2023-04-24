package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateSeatRequest {

    private Integer usedSeat;
    private String updatedByEmail;
    @Pattern(regexp = "\\b(FILL|REVIEW|COMPLETE)\\b", message = "Need to be FILL, REVIEW or COMPLETE")
    private String status;
}
