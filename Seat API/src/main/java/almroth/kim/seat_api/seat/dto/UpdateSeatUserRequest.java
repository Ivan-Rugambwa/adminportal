package almroth.kim.seat_api.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSeatUserRequest {

    @NotNull(message = "Seat amount is required")
    private Integer usedSeat;
    @NotBlank(message = "Email is required")
    private String updatedByEmail;
}
