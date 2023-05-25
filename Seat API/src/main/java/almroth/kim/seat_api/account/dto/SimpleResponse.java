package almroth.kim.seat_api.account.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse {

    private UUID uuid;
    private UUID businessUuid;
    private String email;
    private String firstName;
    private String lastName;
    private String businessName;
    private Set<String> roleNames;
}
