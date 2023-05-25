package almroth.kim.seat_api.account;

import almroth.kim.seat_api.account.dto.SimpleResponse;
import almroth.kim.seat_api.account.dto.UpdateAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/user")
public class AccountController {
    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public List<SimpleResponse> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/only")
    public List<SimpleResponse> getUserAccounts() {
        return accountService.getUserAccounts();
    }

    @GetMapping("/by/business/name/{businessName}")
    public ResponseEntity<?> getAccountsByBusinessName(@PathVariable String businessName) {
        return ResponseEntity.ok(accountService.getAccountsByBusiness(businessName));
    }

    @GetMapping(path = {"{accountId}"})
    public ResponseEntity<?> getAccountById(@PathVariable("accountId") String uuid) {
        return ResponseEntity.ok(accountService.getSimpleAccountByUuid(uuid));
    }

    @PatchMapping(path = "/{accountId}")
    public ResponseEntity<?> putAccount(@PathVariable UUID accountId, @Validated @RequestBody UpdateAccountRequest request) {

        return ResponseEntity.ok(accountService.updateAccount(accountId, request));
    }

    @DeleteMapping(path = "{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountId) {
        accountService.removeAccountByUUID(UUID.fromString(accountId));
        return ResponseEntity.noContent().build();
    }
}
