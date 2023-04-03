package almroth.kim.gamendo_user_api.auth;

import almroth.kim.gamendo_user_api.auth.data.AuthenticationRequest;
import almroth.kim.gamendo_user_api.auth.data.RefreshTokenRequest;
import almroth.kim.gamendo_user_api.auth.data.RegisterRequest;
import almroth.kim.gamendo_user_api.auth.data.ValidateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@PreAuthorize("hasAnyAuthority('Admin', 'User')")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {

        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to login");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user with email found");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad password or username");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("bad " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody ValidateRequest token) {
        var result = service.validateAccessToken(token);
        return ResponseEntity.status(result.getStatus()).body(result.getMessage());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(service.refreshToken(refreshToken.refreshToken));
    }
}
