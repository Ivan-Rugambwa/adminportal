package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.business.dto.BusinessResponse;
import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/business")
@RequiredArgsConstructor
//@EnableMethodSecurity
//@PreAuthorize("hasRole('USER')")
public class BusinessController {
    final BusinessService service;

    @GetMapping
    public List<BusinessResponse> getBusinesses() {
        return service.GetAll();
    }

    @PostMapping
    public void postBusiness(@RequestBody CreateBusinessRequest request) {
        service.Create(request);
    }
}
