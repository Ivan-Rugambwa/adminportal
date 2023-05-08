package almroth.kim.gamendo_user_api.business.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BusinessResponse {
    private UUID uuid;
    private String name;
    private Integer seatBaseline;
    private String emailFrequency;

}
