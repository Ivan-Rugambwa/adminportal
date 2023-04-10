package almroth.kim.gamendo_user_api.seat;

import almroth.kim.gamendo_user_api.seat.dto.CreateSeatRequest;
import almroth.kim.gamendo_user_api.seat.dto.UpdateSeatRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/admin/seat")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SeatController {

    final SeatService service;

    @GetMapping
    public ResponseEntity<?> GetAll(){
        var seats = service.GetAllSeats();
        return ResponseEntity.ok().body(seats);
    }
    @GetMapping({"{uuid}"})
    public ResponseEntity<?> GetByUuid(@PathVariable UUID uuid, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.GetByUuid(uuid));
    }

//    @GetMapping(path = "/business/name/{name}")
//    public ResponseEntity<?> GetCurrentMonthSeatByBusinessName(@PathVariable String name){
//        var seats = service.GetSeatByCurrentMonthAndBusinessName(name);
//        return ResponseEntity.ok().body(seats);
//    }
    @GetMapping(path = "/business/{uuid}")
    public ResponseEntity<?> GetAllByBusinessUuid(@PathVariable UUID uuid){
        var seats = service.GetAllSeatsByBusinessUuid(uuid);
        return ResponseEntity.ok().body(seats);
    }
    @PostMapping
    public ResponseEntity<?> CreateBase(@Valid @RequestBody CreateSeatRequest request){
        var seat = service.CreateSeatBase(request);
        return ResponseEntity.ok().body(seat);
    }
    @PatchMapping(path = "{seatUuid}")
    public ResponseEntity<?> Update(@Valid @RequestBody UpdateSeatRequest request, @PathVariable UUID seatUuid){
        service.UpdateSeat(request, seatUuid);
        return ResponseEntity.noContent().build();
    }
}