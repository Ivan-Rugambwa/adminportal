package almroth.kim.seat_api.mapper;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.business.model.Business;
import almroth.kim.seat_api.seat.dto.SeatResponse;
import almroth.kim.seat_api.seat.model.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    Seat SEAT(SeatResponse seatResponse);

    @Mapping(target = "businessName", source = "business", qualifiedByName = "businessToBusinessName")
    @Mapping(target = "completedByEmail", source = "completedBy", qualifiedByName = "accountToAccountName")
    @Mapping(target = "businessBaseline", source = "business", qualifiedByName = "businessToBusinessBaseline")
    @Mapping(target = "emailFrequency", source = "business", qualifiedByName = "businessToBusinessEmailFrequency")
    SeatResponse SEAT_RESPONSE(Seat seat);

    @Named("businessToBusinessName")
    default String businessToBusinessName(Business business) {
        return business.getName();
    }

    @Named("businessToBusinessBaseline")
    default Integer businessToBusinessBaseline(Business business) {
        return business.getSeatBaseline();
    }

    @Named("businessToBusinessEmailFrequency")
    default String businessToBusinessEmailFrequency(Business business) {
        return business.getEmailFrequency();
    }

    @Named("accountToAccountName")
    default String accountToAccountName(Account account) {
        if (account == null) return null;
        return account.getEmail();
    }
}
