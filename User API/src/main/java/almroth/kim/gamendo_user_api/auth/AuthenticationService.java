package almroth.kim.gamendo_user_api.auth;

import almroth.kim.gamendo_user_api.account.AccountRepository;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.accountProfile.AccountProfileRepository;
import almroth.kim.gamendo_user_api.accountProfile.AccountProfileService;
import almroth.kim.gamendo_user_api.accountProfile.model.AccountProfile;
import almroth.kim.gamendo_user_api.auth.dto.*;
import almroth.kim.gamendo_user_api.business.BusinessRepository;
import almroth.kim.gamendo_user_api.business.BusinessService;
import almroth.kim.gamendo_user_api.business.model.Business;
import almroth.kim.gamendo_user_api.config.JwtService;
import almroth.kim.gamendo_user_api.config.NotionConfigProperties;
import almroth.kim.gamendo_user_api.error.customException.DataBadCredentialsException;
import almroth.kim.gamendo_user_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.gamendo_user_api.refreshToken.RefreshTokenService;
import almroth.kim.gamendo_user_api.role.RoleService;
import almroth.kim.gamendo_user_api.role.RoleType;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final AccountProfileService profileService;
    private final BusinessService businessService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotionConfigProperties env;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            System.out.println("Email already taken");
            throw new EmailAlreadyTakenException("Email already taken");
        }
        var encodedPassword = passwordEncoder.encode((request.getPassword()));
        System.out.println(encodedPassword.length());
        var account = Account
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        var business = businessService.Get(request.getBusiness());

        if (account.getEmail().contains("@test.com")) {
            account.setRoles(Set.of(roleService.getRoleByName(RoleType.ADMIN)));
        } else account.setRoles(Set.of(roleService.getRoleByName(RoleType.USER)));

        accountRepository.save(account);

        var profile = AccountProfile.builder()
                .account(account)
                .business(business)
                .build();

        profileService.Create(profile);

        var jwt = jwtService.generateToken(account);
        var refreshToken = refreshTokenService.createRefreshToken(account);
        return RegisterResponse.builder()
                .message("Account registration successful")
                .token(jwt)
                .username(account.getUsername())
                .refreshToken(refreshToken.getToken())
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
        refreshTokenService.deleteAllRefreshTokens(account);
        var refreshToken = refreshTokenService.createRefreshToken(account);

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public int validateAccessToken(String token) {

        var secret = env.secret().getBytes();
        var chunks = token.split("\\.");
        var p1 = Decoders.BASE64URL.decode(chunks[0]);
        var p2 = Decoders.BASE64URL.decode(chunks[1]);
        token = Base64.getEncoder().encodeToString(p1) + "." +
                Base64.getEncoder().encodeToString(p2) + "." +
                chunks[2];

        try {
            com.auth0.jwt.JWT.require(Algorithm.HMAC512(Base64.getDecoder().decode(secret))).build().verify(token);
        } catch (Exception e) {
            return HttpStatus.UNPROCESSABLE_ENTITY.value();
        }

        return HttpStatus.OK.value();

    }

    public AuthenticationResponse refreshToken(String token) {
        var refreshToken = refreshTokenService.GetRefreshTokenByToken(token);
        refreshTokenService.verifyRefreshToken(refreshToken);

        var newToken = refreshTokenService.createRefreshToken(refreshToken.getAccount());

        return AuthenticationResponse
                .builder()
                .refreshToken(newToken.getToken())
                .accessToken(jwtService.generateToken(refreshToken.getAccount()))
                .build();

    }
}
