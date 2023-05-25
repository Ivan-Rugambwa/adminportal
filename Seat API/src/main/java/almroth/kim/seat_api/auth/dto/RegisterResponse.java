package almroth.kim.seat_api.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
    private String message;
    private UUID uuid;
    private String username;
    private String token;
    private String refreshToken;
}
