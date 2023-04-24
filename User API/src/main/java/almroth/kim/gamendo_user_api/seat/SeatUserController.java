package almroth.kim.gamendo_user_api.seat;

import almroth.kim.gamendo_user_api.seat.dto.UpdateSeatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user/seat")
@RequiredArgsConstructor
public class SeatUserController {
    final SeatService service;

    @GetMapping("{uuid}")
    public ResponseEntity<?> GetSeatIfMatchingBusiness(@PathVariable UUID uuid, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.GetByUuidWithMatchingBusiness(uuid, token));
    }

    @GetMapping(path = "/business/{name}")
    public ResponseEntity<?> GetAllByBusinessName(@PathVariable String name, @RequestHeader("Authorization") String token) {
        var seats = service.GetAllSeatsByBusinessName(name, token);
        return ResponseEntity.ok().body(seats);
    }

    @PatchMapping(path = "{uuid}")
    public ResponseEntity<?> Update(@Valid @RequestBody UpdateSeatRequest request, @PathVariable UUID uuid) {
        service.UpdateSeatUser(request, uuid);
        return ResponseEntity.noContent().build();
    }
}
