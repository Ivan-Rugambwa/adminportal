package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.seat.dto.SeatResponse;
import almroth.kim.gamendo_user_api.seat.model.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat SEAT(SeatResponse seatResponse);

    @Mapping(target = "businessName", source = "business", qualifiedByName = "businessToBusinessName")
    @Mapping(target = "completedByEmail", source = "completedBy", qualifiedByName = "accountToAccountName")
    SeatResponse SEAT_RESPONSE(Seat seat);

    @Named("businessToBusinessName")
    default String businessToBusinessName(Business business) {
        return business.getName();
    }
    @Named("accountToAccountName")
    default String accountToAccountName(Account account) {
        if (account == null) return null;
        return account.getEmail();
    }
}
