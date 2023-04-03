package almroth.kim.gamendo_user_api.business.model;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.persistence.*;
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

}
