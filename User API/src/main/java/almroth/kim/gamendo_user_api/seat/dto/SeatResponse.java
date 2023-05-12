package almroth.kim.gamendo_user_api.seat.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class SeatResponse {
    private UUID uuid;
    private String businessName;
    private Integer businessBaseline;
    private String emailFrequency;
    private String completedByEmail;
    private String status;
    private Date lastChangeDate;
    private Integer seatUsed;
    private String forYearMonth;
}
