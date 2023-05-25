package almroth.kim.seat_api.mapper;

import almroth.kim.seat_api.business.dto.CreateBusinessRequest;
import almroth.kim.seat_api.business.dto.BusinessResponse;
import almroth.kim.seat_api.business.model.Business;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    BusinessResponse TO_RESPONSE(Business business);

    Business TO_MODEL(CreateBusinessRequest businessRequest);
}
