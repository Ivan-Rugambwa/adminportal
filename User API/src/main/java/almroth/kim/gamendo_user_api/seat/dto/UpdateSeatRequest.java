package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSeatRequest {
    @NotNull
    private Integer usedSeat;
    @NotNull
    private String updatedByEmail;
}
