package almroth.kim.seat_api.passwordReset;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/password/reset")
@RequiredArgsConstructor
@Validated
public class PasswordResetUserController {
    private final PasswordResetService service;

    @RequestMapping(value = "/exists/{uuid}", method = RequestMethod.HEAD)
    public ResponseEntity<?> authenticate(@PathVariable UUID uuid) {

        var response = service.doesResetPasswordExist(uuid);

        if (response)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
