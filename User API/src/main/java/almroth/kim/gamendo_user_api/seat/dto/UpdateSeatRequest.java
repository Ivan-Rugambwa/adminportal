package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSeatRequest {
    @NotNull
    private Integer usedSeat;
    @NotBlank
    private String updatedByEmail;
    @NotBlank
    private String status;
}
