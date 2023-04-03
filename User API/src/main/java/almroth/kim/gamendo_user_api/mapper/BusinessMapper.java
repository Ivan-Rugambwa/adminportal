package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import almroth.kim.gamendo_user_api.business.dto.BusinessResponse;
import almroth.kim.gamendo_user_api.business.model.Business;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    BusinessResponse TO_RESPONSE(Business business);
    Business TO_MODEL(CreateBusinessRequest businessRequest);
}
