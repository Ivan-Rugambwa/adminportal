package almroth.kim.gamendo_user_api.business.model;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID uuid;
    private String name;
    @OneToMany
    private Set<AccountProfile> accountProfiles;

}
