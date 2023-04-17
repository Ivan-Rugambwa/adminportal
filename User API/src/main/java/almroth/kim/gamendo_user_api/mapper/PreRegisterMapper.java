package almroth.kim.gamendo_user_api.mapper;

import almroth.kim.gamendo_user_api.business.dto.BusinessResponse;
import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterResponse;
import almroth.kim.gamendo_user_api.preRegister.model.PreRegister;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreRegisterMapper {
    PreRegisterResponse TO_RESPONSE(PreRegister preRegister);
    PreRegister TO_MODEL(PreRegisterRequest preRegisterRequest);
}
