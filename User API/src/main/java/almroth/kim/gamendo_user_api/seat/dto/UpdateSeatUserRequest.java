package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateSeatUserRequest {

    @NotNull(message = "Seat amount is required")
    private Integer usedSeat;
    @NotBlank(message = "Email is required")
    private String updatedByEmail;
}
