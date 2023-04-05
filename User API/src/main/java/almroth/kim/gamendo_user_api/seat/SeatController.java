package almroth.kim.gamendo_user_api.seat;

import almroth.kim.gamendo_user_api.seat.dto.CreateSeatRequest;
import almroth.kim.gamendo_user_api.seat.dto.UpdateSeatRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/admin/seat")
@RequiredArgsConstructor
public class SeatController {

    final SeatService service;

    @GetMapping
    public ResponseEntity<?> GetAll(){
        var seats = service.GetAllSeats();
        return ResponseEntity.ok().body(seats);
    }
    @GetMapping(path = "/business/{name}")
    public ResponseEntity<?> GetAllByBusinessName(@PathVariable String name){
        var seats = service.GetAllSeatsByBusinessName(name);
        return ResponseEntity.ok().body(seats);
    }
    @PostMapping
    public ResponseEntity<?> CreateBase(@RequestBody CreateSeatRequest request){
        var seat = service.CreateSeatBase(request);
        return ResponseEntity.ok().body(seat);
    }
    @PatchMapping(path = "{seatUuid}")
    public ResponseEntity<?> Update(@RequestBody UpdateSeatRequest request, @PathVariable UUID seatUuid){
        service.UpdateSeat(request, seatUuid);
        return ResponseEntity.noContent().build();
    }
}