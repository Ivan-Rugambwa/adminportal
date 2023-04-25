package almroth.kim.gamendo_user_api.business;

import almroth.kim.gamendo_user_api.business.dto.BusinessResponse;
import almroth.kim.gamendo_user_api.business.dto.CreateBusinessRequest;
import almroth.kim.gamendo_user_api.business.dto.UpdateBusinessRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/business")
@RequiredArgsConstructor
public class BusinessController {
    final BusinessService service;

    @GetMapping
    public ResponseEntity<?> getBusinesses() {
        return ResponseEntity.ok(service.GetAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getBusiness(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.GetResponseByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<?> postBusiness(@Valid @RequestBody CreateBusinessRequest request) {
        return ResponseEntity.ok(service.Create(request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteBusiness(@PathVariable UUID uuid) {
        service.Delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateBusiness(@RequestBody UpdateBusinessRequest request, @PathVariable UUID uuid) {
        var response = service.Update(request, uuid);
        return ResponseEntity.ok(response);
    }
}
