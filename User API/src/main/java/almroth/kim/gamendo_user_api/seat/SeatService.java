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

import java.time.Instant;
import java.util.*;

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
        System.out.println("Creating seat...");
        var business = businessService.GetByUuid(request.getBusinessUuid());
        var account = accountService.getAccountByUuid(request.getAccountUuid());

        var seats = repository.findAllByBusiness_Uuid(business.getUuid()).orElse(new HashSet<>());
        if (seats.stream().anyMatch(seat -> Objects.equals(seat.getForYearMonth(), request.getForYearMonth()))) {
            System.out.println("Seat already exists");
            throw new IllegalArgumentException("There is already a seat report for that year and month with that business");
        }

        var seat = Seat.builder()
                .seatUsed(null)
                .assignedAccount(account)
                .business(business)
                .isCompleted(false)
                .forYearMonth(request.getForYearMonth())
                .build();
        var savedSeat = repository.save(seat);
        System.out.println("Successfully created seat.");
        return mapper.SEAT_RESPONSE(savedSeat);
    }
    public void UpdateSeat(UpdateSeatRequest request, UUID seatUuid){
        var seat = repository.findById(seatUuid).orElseThrow(() -> new IllegalArgumentException("No seat with id: " + seatUuid));

        seat.setSeatUsed(request.getUsedSeat());
        seat.setIsCompleted(true);
        seat.setLastChangeDate(Date.from(Instant.now()));
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
    public Set<SeatResponse> GetAllSeatsByBusinessUuid(UUID uuid) {
        var seats = repository.findAllByBusiness_Uuid(uuid).orElseThrow(() -> new IllegalArgumentException("No seats with business uuid: " + uuid));
        Set<SeatResponse> seatResponses = new HashSet<>();
        for (var seat :
                seats) {
            seatResponses.add(mapper.SEAT_RESPONSE(seat));
        }
        return seatResponses;
    }
}
