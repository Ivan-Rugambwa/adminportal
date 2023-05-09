package almroth.kim.gamendo_user_api.business.model;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.seat.model.Seat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "business")
    private Set<AccountProfile> accountProfiles = new HashSet<>();
    private Integer seatBaseline;
    @OneToMany(mappedBy = "business")
    private Set<Seat> seats;
    @Pattern(regexp = "^(MONTHLY|QUARTERLY|SEMI|ANNUALLY)$", message = "Need to be MONTHLY, QUARTERLY, SEMI or ANNUALLY")
    private String emailFrequency;

}
