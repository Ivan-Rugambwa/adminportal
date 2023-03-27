package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.data.LoginRequest;
import almroth.kim.gamendo_user_api.account.data.SimpleResponse;
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

    @GetMapping(path = {"{accountId}"})
    public Account getAccountById(@PathVariable("accountId") String uuid) {
        try {
            return accountService.getAccountByUuid(uuid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Account();
        }
    }

    @PostMapping
    public void postAccount(@RequestBody Account account) {
        accountService.addAccount(account);
    }

    @DeleteMapping(path = {"{accountId}"})
    public void deleteAccount(@PathVariable("accountId") String uuid) {
        accountService.removeAccountByUUID(UUID.fromString(uuid));
    }

    @PutMapping(path = "{accountId}")
    public void putAccount(@PathVariable String accountId, @RequestBody Account values) {
        accountService.updateAccount(accountId, values);
    }

    @PostMapping("/login")
    public Account login(@RequestBody LoginRequest account) {
        return accountService.login(account);
    }
}
