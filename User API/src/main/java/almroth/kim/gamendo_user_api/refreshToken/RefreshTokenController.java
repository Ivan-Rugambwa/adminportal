package almroth.kim.gamendo_user_api.refreshToken;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/refresh")
@AllArgsConstructor
public class RefreshTokenController {

    RefreshTokenService service;

    @GetMapping
    public ResponseEntity<?> getTokenByToken(@RequestParam String uuid) {
        return ResponseEntity.ok().body(service.GetRefreshTokenByToken(uuid));
    }
}
