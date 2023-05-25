package almroth.kim.seat_api.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusinessRequest {
    @NotBlank
    private String name;
    @NotNull
    private Integer seatBaseline;
    @NotBlank(message = "Email frequency can not be empty, Need to be MONTHLY, QUARTERLY, SEMI or ANNUALLY")
    @Pattern(regexp = "^(MONTHLY|QUARTERLY|SEMI|ANNUALLY)$", message = "Need to be MONTHLY, QUARTERLY, SEMI or ANNUALLY")
    private String emailFrequency;
}
