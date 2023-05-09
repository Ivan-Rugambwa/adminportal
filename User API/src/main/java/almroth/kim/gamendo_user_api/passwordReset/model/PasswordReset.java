package almroth.kim.gamendo_user_api.passwordReset.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset {

    @Id
    @UuidGenerator
    private UUID uuid;

    @OneToOne
    @NotNull(message = "Account is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

    @NotNull(message = "Date is required")
    private Date createdAtDate;
}
