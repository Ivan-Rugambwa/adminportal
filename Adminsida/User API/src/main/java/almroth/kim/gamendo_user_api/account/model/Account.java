package almroth.kim.gamendo_user_api.account.model;

import almroth.kim.gamendo_user_api.refreshToken.model.RefreshToken;
import almroth.kim.gamendo_user_api.role.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ACCOUNT_ROLES",
            joinColumns = {
                    @JoinColumn(name = "ACCOUNT_UUID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")})
    private Set<Role> roles;

    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private RefreshToken refreshToken;

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
