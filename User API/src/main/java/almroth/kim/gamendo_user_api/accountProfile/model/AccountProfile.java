package almroth.kim.gamendo_user_api.accountProfile.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.model.Business;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfile {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Business business;
    @OneToOne()
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

}
