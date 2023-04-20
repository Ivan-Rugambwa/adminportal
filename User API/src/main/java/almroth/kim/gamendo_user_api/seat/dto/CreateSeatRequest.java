package almroth.kim.gamendo_user_api.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateSeatRequest {
    @NotBlank(message = "Business uuid is required")
    private UUID businessUuid;
    @NotBlank(message = "Year and Month is required")
    @Pattern(regexp = "^\\d{4}/(0[1-9]|1[0-2])$", message = "forYearMonth: Need to be in the form of a 4 digit year and a 2 digit month separated with a slash, for example: 2023/04")
    private String forYearMonth;
}
