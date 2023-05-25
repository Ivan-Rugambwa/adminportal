package almroth.kim.seat_api.refreshToken.model;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.config.ColumnEncryptor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Convert(converter = ColumnEncryptor.class)
    private String token;
    @Column
    private Instant expirationDateInMilliSeconds;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_UUID", nullable = false)
    @JsonBackReference
    private Account account;
    @Column
    private boolean isExpiredByNewToken;


}
