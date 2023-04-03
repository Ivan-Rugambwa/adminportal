package almroth.kim.gamendo_user_api.account.dto;

import almroth.kim.gamendo_user_api.role.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse {

    private String email;
    private String firstName;
    private String lastName;
}
