package almroth.kim.gamendo_user_api.auth;

import almroth.kim.gamendo_user_api.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterWithPreRegisterRequest request) {
        return ResponseEntity.ok(service.registerWithPreRegister(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody ValidateRequest request) {
        var result = service.validateAccessToken(request.getToken());
        return ResponseEntity.status(result).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(service.refreshToken(refreshToken.refreshToken));
    }

    @PostMapping("/reset/start")
    public ResponseEntity<?> startResetPassword(@RequestBody ResetRequest request) {
        service.startResetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset/finish")
    public ResponseEntity<?> startResetPassword(@RequestBody FinishResetRequest request) {
        service.finishResetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        service.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}
