package almroth.kim.gamendo_user_api.seat.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreateSeatRequest {
    private String businessUuid;
    private String accountUuid;
}
