package almroth.kim.seat_api.accountProfile.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AddAccountProfileViewModel {
    private String businessName;
    private Set<String> accountEmail;
}
