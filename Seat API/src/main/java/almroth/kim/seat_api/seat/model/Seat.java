package almroth.kim.seat_api.seat.model;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.business.model.Business;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @UuidGenerator
    private UUID uuid;
    @ManyToOne
    @NotNull(message = "Business is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Business business;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account completedBy;
    @NotNull(message = "Status is required")
    @Pattern(regexp = "\\b(FILL|REVIEW|COMPLETE)\\b", message = "Need to be FILL, REVIEW or COMPLETE")
    private String status;

    private Date lastChangeDate;
    @NotNull
    @NotBlank(message = "Year and Month is required")
    @Pattern(regexp = "^\\d{4}/(0[1-9]|1[0-2])$", message = "forYearMonth: Need to be in the form of a 4 digit year and a 2 digit month separated with a slash, for example: 2023/04")
    private String forYearMonth;
    private Integer seatUsed;
}
