package almroth.kim.gamendo_user_api.seat.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SeatResponse {
    private String businessName;
    private String assignedAccountEmail;
    private Boolean isCompleted;
    private Date lastChangeDate;
    private Integer seatUsed;
}
