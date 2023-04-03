package almroth.kim.gamendo_user_api.auth;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.auth.data.*;
import almroth.kim.gamendo_user_api.config.JwtService;
import almroth.kim.gamendo_user_api.config.NotionConfigProperties;
import almroth.kim.gamendo_user_api.error.customException.DataBadCredentialsException;
import almroth.kim.gamendo_user_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.gamendo_user_api.refreshToken.RefreshTokenService;
import almroth.kim.gamendo_user_api.role.RoleService;
import almroth.kim.gamendo_user_api.role.RoleType;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotionConfigProperties env;

    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            System.out.println("Email already taken");
            throw new EmailAlreadyTakenException("Email already taken");
        }
        var account = Account
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode((request.getPassword())))
                .build();

        account.setRoles(Set.of(roleService.getRoleByName(RoleType.USER)));
        if (account.getEmail().contains("@test.com")) {
            account.setRoles(Set.of(roleService.getRoleByName(RoleType.ADMIN)));
        }
        var jwt = jwtService.generateToken(account);
        accountRepository.save(account);
        refreshTokenService.createRefreshToken(account);
        return RegisterResponse.builder()
                .message("Account registration successful")
                .token(jwt)
                .username(account.getUsername())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("In Authenticate");
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("No account found with email: " + request.getEmail()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new DataBadCredentialsException("Wrong username or password", request.getEmail(), request.getPassword());
        }
        var jwt = jwtService.generateToken(account);
        if (account.getRefreshToken() == null) {
            refreshTokenService.createRefreshToken(account);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .build();

    }

    public ValidateResponse validateAccessToken(ValidateRequest validateRequest) {

        var secret = Base64.getDecoder().decode(env.secret().getBytes());

        try {
            DecodedJWT decodedJWT = com.auth0.jwt.JWT.require(Algorithm.HMAC512(secret)).build().verify(validateRequest.getToken());
        } catch (Exception e) {
            return ValidateResponse
                    .builder()
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .message("Token invalid")
                    .build();
        }

        return ValidateResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Token valid")
                .build();

    }

    public AuthenticationResponse refreshToken(String token) {
        var refreshToken = refreshTokenService.GetRefreshTokenByToken(token);
        refreshToken = refreshTokenService.verifyRefreshToken(refreshToken);

        refreshTokenService.deleteRefreshTokenByUserId(refreshToken.getAccount().getUuid());
        var newToken = refreshTokenService.createRefreshToken(refreshToken.getAccount());

        return AuthenticationResponse
                .builder()
                .refreshToken(newToken.getToken())
                .accessToken(jwtService.generateToken(refreshToken.getAccount()))
                .build();

    }
}
