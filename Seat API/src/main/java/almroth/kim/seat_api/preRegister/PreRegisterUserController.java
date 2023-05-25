package almroth.kim.seat_api.preRegister;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/auth/preregister")
@RequiredArgsConstructor
public class PreRegisterUserController {

    final PreRegisterService service;

    @GetMapping({"{uuid}"})
    public ResponseEntity<?> GetByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.GetByUuid(uuid));
    }

}