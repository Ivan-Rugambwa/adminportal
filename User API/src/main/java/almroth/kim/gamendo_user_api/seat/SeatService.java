package almroth.kim.gamendo_user_api.seat;

import almroth.kim.gamendo_user_api.account.AccountService;
import almroth.kim.gamendo_user_api.business.BusinessService;
import almroth.kim.gamendo_user_api.mapper.SeatMapper;
import almroth.kim.gamendo_user_api.seat.dto.CreateSeatRequest;
import almroth.kim.gamendo_user_api.seat.dto.SeatResponse;
import almroth.kim.gamendo_user_api.seat.dto.UpdateSeatRequest;
import almroth.kim.gamendo_user_api.seat.model.Seat;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Data
public class SeatService {
    @Autowired
    private final SeatRepository repository;
    @Autowired
    private final BusinessService businessService;
    @Autowired
    private final AccountService accountService;
    private final SeatMapper mapper = Mappers.getMapper(SeatMapper.class);

    public Set<SeatResponse> GetAllSeats(){
        var seats = repository.findAll();
        Set<SeatResponse> seatResponses = new HashSet<>();

        for (var seat :
                seats) {
            seatResponses.add(mapper.SEAT_RESPONSE(seat));
        }
        return seatResponses;
    }
    public SeatResponse CreateSeatBase(CreateSeatRequest request){
        var business = businessService.GetByUuid(request.getBusinessUuid());
        var account = accountService.getAccountByUuid(request.getAccountUuid());

        var seat = Seat.builder()
                .seatUsed(null)
                .assignedAccount(account)
                .business(business)
                .isCompleted(false)
                .build();
        repository.save(seat);
        return mapper.SEAT_RESPONSE(seat);
    }
    public void UpdateSeat(UpdateSeatRequest request, String seatUuid){
        var seat = repository.findById(UUID.fromString(seatUuid)).orElseThrow(() -> new IllegalArgumentException("No seat with id: " + seatUuid));

        seat.setSeatUsed(request.getUsedSeat());
        seat.setIsCompleted(true);
        repository.save(seat);
    }

    public Set<SeatResponse> GetAllSeatsByBusinessName(String name) {
        var seats = repository.findAllByBusiness_Name(name).orElseThrow(() -> new IllegalArgumentException("No seats with business: " + name));
        Set<SeatResponse> seatResponses = new HashSet<>();
        for (var seat :
                seats) {
            seatResponses.add(mapper.SEAT_RESPONSE(seat));
        }
        return seatResponses;
    }
}
