package almroth.kim.gamendo_user_api.refreshToken.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String token;
    @Column
    private Instant expirationDateInMilliSeconds;
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "uuid")
    @JsonBackReference
    private Account account;
}
