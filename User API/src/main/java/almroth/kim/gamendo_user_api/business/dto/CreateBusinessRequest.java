package almroth.kim.gamendo_user_api.business.dto;

import lombok.Data;

@Data
public class CreateBusinessRequest {
    private String name;
    private Integer seatAmount;
}
