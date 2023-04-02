package almroth.kim.gamendo_user_api.accountProfile.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.model.Business;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID uuid;

    @ManyToOne
    private Business business;

    @OneToOne(mappedBy = "profile")
    private Account account;

}
