package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.dto.UpdateAccountRequest;
import almroth.kim.gamendo_user_api.account.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/user")
//@EnableMethodSecurity
//@PreAuthorize("hasRole('USER')")
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

    @GetMapping(path = {"{accountId}"})
    public Account getAccountById(@PathVariable("accountId") String uuid) {
        try {
            return accountService.getAccountByUuid(uuid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Account();
        }
    }
    @PutMapping(path = "{accountId}")
    public void putAccount(@PathVariable String accountId, @RequestBody UpdateAccountRequest request) {
        accountService.updateAccount(accountId, request);
    }
    @DeleteMapping(path = "{accountId}")
    public void deleteAccount(@PathVariable String accountId){
        accountService.removeAccountByUUID(UUID.fromString(accountId));
    }
}
