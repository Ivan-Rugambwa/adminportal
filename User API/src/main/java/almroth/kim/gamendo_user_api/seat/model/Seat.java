package almroth.kim.gamendo_user_api.seat.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.model.Business;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @UuidGenerator
    private String uuid;
    @ManyToOne
    @NotNull(message = "Business is required")
    private Business business;
    @ManyToOne
    private Account assignedAccount;
    @NotNull(message = "Is completed is required")
    private Boolean isCompleted;
    @LastModifiedDate
    private Date lastChangeDate;
    private Integer seatUsed;
}
