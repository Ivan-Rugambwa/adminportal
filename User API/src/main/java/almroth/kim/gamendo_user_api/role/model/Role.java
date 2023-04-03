package almroth.kim.gamendo_user_api.role.model;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.role.RoleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Collection<Account> accounts;

}
