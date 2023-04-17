package almroth.kim.gamendo_user_api.preRegister;

import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.gamendo_user_api.seat.dto.CreateSeatRequest;
import almroth.kim.gamendo_user_api.seat.dto.UpdateSeatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/admin/preregister")
@RequiredArgsConstructor
public class PreRegisterController {

    final PreRegisterService service;

    @GetMapping
    public ResponseEntity<?> GetAll(){
        var seats = service.GetAllPreRegisters();
        return ResponseEntity.ok().body(seats);
    }
    @GetMapping({"{uuid}"})
    public ResponseEntity<?> GetByUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(service.GetByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<?> Create(@Valid @RequestBody PreRegisterRequest request){
        var seat = service.Create(request);
        return ResponseEntity.ok().body(seat);
    }
    @PatchMapping("{uuid}")
    public ResponseEntity<?> Update(@Valid @RequestBody PreRegisterRequest request, @PathVariable UUID uuid){
        var seat = service.Update(request, uuid);
        return ResponseEntity.ok().body(seat);
    }
    @DeleteMapping("{uuid}")
    public ResponseEntity<?> Delete(@PathVariable UUID uuid){
        service.Delete(uuid);
        return ResponseEntity.noContent().build();
    }
}