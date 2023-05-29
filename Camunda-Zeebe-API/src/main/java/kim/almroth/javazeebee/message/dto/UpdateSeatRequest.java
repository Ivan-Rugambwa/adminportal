package kim.almroth.javazeebee.message.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateSeatRequest {
    private String updatedByEmail;
    private Integer usedSeat;
    private String status;
}
