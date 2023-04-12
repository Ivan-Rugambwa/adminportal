package almroth.kim.gamendo_user_api.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusinessRequest {
    private String name;
    private Integer seatBaseline;
}
