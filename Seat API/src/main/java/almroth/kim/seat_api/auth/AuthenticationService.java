package almroth.kim.seat_api.auth;

import almroth.kim.seat_api.account.AccountRepository;
import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.accountProfile.AccountProfileService;
import almroth.kim.seat_api.accountProfile.model.AccountProfile;
import almroth.kim.seat_api.auth.dto.*;
import almroth.kim.seat_api.business.BusinessService;
import almroth.kim.seat_api.config.JwtService;
import almroth.kim.seat_api.config.NotionConfigProperties;
import almroth.kim.seat_api.error.customException.DataBadCredentialsException;
import almroth.kim.seat_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.seat_api.error.customException.PasswordResetTimeoutException;
import almroth.kim.seat_api.passwordReset.PasswordResetService;
import almroth.kim.seat_api.preRegister.PreRegisterService;
import almroth.kim.seat_api.refreshToken.RefreshTokenService;
import almroth.kim.seat_api.role.RoleService;
import almroth.kim.seat_api.role.RoleType;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordResetService passwordResetService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final AccountProfileService profileService;
    private final BusinessService businessService;
    private final PreRegisterService preRegisterService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotionConfigProperties env;

    @Transactional
    public RegisterResponse registerWithPreRegister(RegisterWithPreRegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password must match");
        }
        var preRegister = preRegisterService.GetByUuid(request.getPreRegisterUuid());

        var registerRequest = RegisterRequest.builder()
                .email(preRegister.getEmail())
                .password(request.getPassword())
                .firstName(preRegister.getFirstName())
                .lastName(preRegister.getLastName())
                .business(preRegister.getBusinessName())
                .build();

        var isAdmin = (Objects.equals(preRegister.getRoleName(), "ADMIN"));

        var registerResponse = register(registerRequest, isAdmin);
        preRegisterService.DeleteByUuid(preRegister.getUuid());
        return registerResponse;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request, boolean isAdmin) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            System.out.println("Email already taken");
            throw new EmailAlreadyTakenException("Email already taken");
        }

        var encodedPassword = passwordEncoder.encode((request.getPassword()));
        System.out.println(encodedPassword.length());
        var account = Account
                .builder()
                .firstName(StringUtils.capitalize(request.getFirstName()))
                .lastName(StringUtils.capitalize(request.getLastName()))
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        if (isAdmin) {
            account.setRoles(Set.of(roleService.getRoleByName(RoleType.ADMIN)));
        } else
            account.setRoles(Set.of(roleService.getRoleByName(RoleType.USER)));

        accountRepository.save(account);

        if (request.getBusiness() != null) {
            var business = businessService.GetByName(request.getBusiness());
            var profile = AccountProfile.builder()
                    .account(account)
                    .business(business)
                    .build();

            profileService.Create(profile);
        }

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

        if (isLoginInvalid(request.getEmail(), request.getPassword())) {
            throw new DataBadCredentialsException("Wrong username or password", request.getEmail(), request.getPassword());
        }

        var jwt = jwtService.generateToken(account);
        refreshTokenService.deleteAllRefreshTokens(account);
        var refreshToken = refreshTokenService.createRefreshToken(account);
        System.out.println("Successfully authenticated");
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private boolean isLoginInvalid(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );
            return false;
        } catch (BadCredentialsException ex) {
            System.out.println("Wrong username or password");
            return true;
        }
    }

    public int validateAccessToken(String token) {

        var secret = env.secret().getBytes();
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

    @Transactional
    public void changePassword(UpdatePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Password must match");
        }
        if (isLoginInvalid(request.getEmail(), request.getOldPassword())) {
            throw new DataBadCredentialsException("Wrong username or password", request.getEmail(), request.getOldPassword());
        }
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("No account found with email: " + request.getEmail()));
        var encodedPassword = passwordEncoder.encode(request.getNewPassword());
        account.setPassword(encodedPassword);
        accountRepository.save(account);
    }

    public void startResetPassword(ResetRequest request) {
        var account = accountRepository.findByEmail(request.getEmail());
        if (account.isEmpty())
            return;
        var passwordReset = passwordResetService.createPasswordReset(account.get());
        try {
            passwordResetService.sendPasswordResetEmail(account.get().getEmail(), passwordReset.getUuid().toString(), account.get().getFirstName());
        } catch (MessagingException | IOException e) {
            passwordResetService.deletePasswordResetByUuid(passwordReset.getUuid());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Transactional
    public void finishResetPassword(FinishResetRequest request) {
        var passwordReset = passwordResetService.getPasswordResetByUuid(request.getResetPasswordUuid());

        var datePlus = passwordReset.getCreatedAtDate().toInstant().plus(Duration.ofMinutes(30));
        if (Instant.now().isAfter(datePlus)) {
            passwordResetService.deletePasswordResetByUuid(passwordReset.getUuid());
            throw new PasswordResetTimeoutException("This link has expired, please reset your password again.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Password must match");
        }

        var encodedPassword = passwordEncoder.encode(request.getNewPassword());
        passwordReset.getAccount().setPassword(encodedPassword);
        accountRepository.save(passwordReset.getAccount());
        passwordResetService.deletePasswordResetByUuid(passwordReset.getUuid());
    }
}
