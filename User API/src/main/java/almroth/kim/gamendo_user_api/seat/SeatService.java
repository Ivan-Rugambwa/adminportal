package almroth.kim.gamendo_user_api.seat;

import almroth.kim.gamendo_user_api.account.AccountService;
import almroth.kim.gamendo_user_api.business.BusinessService;
import almroth.kim.gamendo_user_api.config.JwtService;
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
    @Autowired
    private final JwtService jwtService;
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
    public SeatResponse GetByUuid(UUID uuid){
        var seat = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such seat"));
        return mapper.SEAT_RESPONSE(seat);
    }
    public SeatResponse GetByUuidWithMatchingBusiness(UUID seatUuid, String token){
        var seat = repository.findById(seatUuid).orElseThrow(() -> new IllegalArgumentException("No such seat"));
        var claim = jwtService.extractClaim(token.substring(7), claims -> claims.get("organization")).toString();
        if (!Objects.equals(seat.getBusiness().getName(), claim)) throw new IllegalArgumentException("Account business and Seat business does not match.");
        return mapper.SEAT_RESPONSE(seat);
    }
    public SeatResponse CreateSeatBase(CreateSeatRequest request){
        System.out.println("Creating seat...");
        var business = businessService.GetByUuid(request.getBusinessUuid());

        var seats = repository.findAllByBusiness_Uuid(business.getUuid()).orElse(new HashSet<>());
        if (seats.stream().anyMatch(seat -> Objects.equals(seat.getForYearMonth(), request.getForYearMonth()))) {
            System.out.println("Seat already exists");
            throw new IllegalArgumentException("There is already a seat report for that year and month with that business");
        }

        var seat = Seat.builder()
                .seatUsed(null)
                .completedBy(null)
                .business(business)
                .status("FILL")
                .forYearMonth(request.getForYearMonth())
                .build();
        var savedSeat = repository.save(seat);
        System.out.println("Successfully created seat.");
        return mapper.SEAT_RESPONSE(savedSeat);
    }
    public void UpdateSeat(UpdateSeatRequest request, UUID seatUuid){
        System.out.println("Updating seat...");
        var seat = repository.findById(seatUuid).orElseThrow(() -> new IllegalArgumentException("No seat with id: " + seatUuid));

        if (request.getUpdatedByEmail() != null){
            var account = accountService.getAccountByEmail(request.getUpdatedByEmail());
            seat.setCompletedBy(account);
        }
        if (request.getUsedSeat() != null) seat.setSeatUsed(request.getUsedSeat());
        if (request.getStatus() != null) seat.setStatus(request.getStatus());
        seat.setLastChangeDate(Date.from(Instant.now()));

        repository.save(seat);
    }

    public Set<SeatResponse> GetAllSeatsByBusinessName(String name, String token) {
        var seats = repository.findAllByBusiness_Name(name).orElseThrow(() -> new IllegalArgumentException("No seats with business: " + name));
        var claim = jwtService.extractClaim(token.substring(7), claims -> claims.get("organization")).toString();

        Set<SeatResponse> seatResponses = new HashSet<>();
        for (var seat : seats) {
            if (Objects.equals(seat.getBusiness().getName(), claim))
                seatResponses.add(mapper.SEAT_RESPONSE(seat));
            else throw new IllegalArgumentException("Account business and Seat business does not match.");
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

//    public SeatResponse GetSeatByCurrentMonthAndBusinessName(String name, String forYearMonth) {
//        var seat = repository.findByBusiness_NameAndForYearMonthEquals(name, forYearMonth);
//    }
}
