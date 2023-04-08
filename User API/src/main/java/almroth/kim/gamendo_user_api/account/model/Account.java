package almroth.kim.gamendo_user_api.account.model;

import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.refreshToken.model.RefreshToken;
import almroth.kim.gamendo_user_api.role.model.Role;
import almroth.kim.gamendo_user_api.seat.model.Seat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Data
public class Account implements UserDetails {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Wrong email format, see correct example: test@domain.com")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 60, max = 60, message = "Password needs to be 60 characters.")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @OneToOne(mappedBy = "account")
    private AccountProfile profile;

    @OneToMany(mappedBy = "completedBy")
    private Set<Seat> completedSeats;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ACCOUNT_ROLES",
            joinColumns = {
                    @JoinColumn(name = "ACCOUNT_UUID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")})
    @JsonManagedReference
    private Set<Role> roles;

    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
