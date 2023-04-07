package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.business.dto.BusinessResponse;
import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @DeleteMapping("{uuid}")
    public ResponseEntity<?> deleteBusiness(@PathVariable UUID uuid){
        service.Delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
