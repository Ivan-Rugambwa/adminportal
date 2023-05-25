package almroth.kim.seat_api.mapper;

import almroth.kim.seat_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.seat_api.preRegister.dto.PreRegisterResponse;
import almroth.kim.seat_api.preRegister.model.PreRegister;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreRegisterMapper {
    PreRegisterResponse TO_RESPONSE(PreRegister preRegister);

    PreRegister TO_MODEL(PreRegisterRequest preRegisterRequest);
}
