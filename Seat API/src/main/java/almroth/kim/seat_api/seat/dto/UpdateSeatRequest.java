package almroth.kim.seat_api.seat.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSeatRequest {

    private Integer usedSeat;
    private String updatedByEmail;
    @Pattern(regexp = "\\b(FILL|REVIEW|COMPLETE)\\b", message = "Need to be FILL, REVIEW or COMPLETE")
    private String status;
}
