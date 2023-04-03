package almroth.kim.gamendo_user_api.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadCredentialsResponse extends ErrorResponse {
    private String username;
    private String password;
}
