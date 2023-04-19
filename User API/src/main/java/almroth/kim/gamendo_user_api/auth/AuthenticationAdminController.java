package almroth.kim.gamendo_user_api.auth;

import almroth.kim.gamendo_user_api.auth.dto.AuthenticationRequest;
import almroth.kim.gamendo_user_api.auth.dto.RefreshTokenRequest;
import almroth.kim.gamendo_user_api.auth.dto.RegisterRequest;
import almroth.kim.gamendo_user_api.auth.dto.ValidateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
public class AuthenticationAdminController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request, true));
    }

}
