package almroth.kim.gamendo_user_api.preRegister;

import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> GetByUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(service.GetByUuid(uuid));
    }

}