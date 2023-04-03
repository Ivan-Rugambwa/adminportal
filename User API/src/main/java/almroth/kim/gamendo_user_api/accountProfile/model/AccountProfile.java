package almroth.kim.gamendo_user_api.accountProfile.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.model.Business;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Business business;
    @OneToOne()
    private Account account;

}
