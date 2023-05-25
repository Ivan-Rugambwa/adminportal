package almroth.kim.seat_api.preRegister;

import almroth.kim.seat_api.account.AccountService;
import almroth.kim.seat_api.config.JwtService;
import almroth.kim.seat_api.config.NotionConfigProperties;
import almroth.kim.seat_api.mapper.PreRegisterMapper;
import almroth.kim.seat_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.seat_api.preRegister.dto.PreRegisterResponse;
import almroth.kim.seat_api.preRegister.dto.UpdatePreRegisterRequest;
import almroth.kim.seat_api.preRegister.model.PreRegister;
import almroth.kim.seat_api.role.RoleService;
import almroth.kim.seat_api.role.RoleType;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Data
public class PreRegisterService {
    @Autowired
    private final PreRegisterRepository repository;
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final RoleService roleService;
    @Autowired
    private final NotionConfigProperties env;

    private final PreRegisterMapper mapper = Mappers.getMapper(PreRegisterMapper.class);

    public Set<PreRegisterResponse> GetAllPreRegisters() {
        var preRegisters = repository.findAll();
        Set<PreRegisterResponse> preRegisterResponses = new HashSet<>();

        for (var preRegister :
                preRegisters) {
            preRegisterResponses.add(mapper.TO_RESPONSE(preRegister));
        }
        return preRegisterResponses;
    }

    public PreRegisterResponse GetByUuid(UUID uuid) {
        var preRegister = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        return mapper.TO_RESPONSE(preRegister);
    }

    @Transactional
    public PreRegisterResponse Create(PreRegisterRequest request) {

        System.out.println("Registering account with email: " + request.getEmail() + " as role: " + request.getRoleName() + "...");

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (accountService.doesAccountExistByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        var preRegisterBuilder = PreRegister.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roleName(request.getRoleName());

        if (Objects.equals(request.getRoleName(), "USER")) {
            if (request.getBusinessName() != null)
                preRegisterBuilder.businessName(request.getBusinessName());
            else
                throw new IllegalArgumentException("Business is required if role is user");
        }

        var preRegister = preRegisterBuilder.build();

        var acc = repository.save(preRegister);
        System.out.println(acc.getUuid());
        try {
            sendMail(request.getEmail(), request.getFirstName(), request.getLastName(), preRegister.getUuid().toString());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        System.out.println("Registered account.");
        return mapper.TO_RESPONSE(preRegister);
    }

    @Transactional
    public void DeleteByUuid(UUID uuid) {
        var preRegister = repository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        repository.delete(preRegister);
    }

    public void sendMail(String toEmail, String firstName, String lastName, String uuid) throws MessagingException, IOException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "mail.smtp2go.com");
        prop.put("mail.smtp.port", "2525");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.email(), env.password());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("apendo.operations@outlook.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Slutför skapande av konto");

        var registerUrl = env.registerUrl() + "?uuid=" + uuid;

        var path = Path.of("../Email/register.html");
        String email = new String(Files.readAllBytes(path));
        email = email.replaceAll(Pattern.quote("{firstName}"), firstName);
        email = email.replaceAll(Pattern.quote("{lastName}"), lastName);
        email = email.replaceAll(Pattern.quote("{registerUrl}"), registerUrl);


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(email, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }

    @Transactional
    public PreRegisterResponse Update(UpdatePreRegisterRequest request, UUID uuid) {
        var mailUpdated = false;
        var preRegister = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        if (request.getBusinessName() != null) preRegister.setBusinessName(request.getBusinessName());
        if (request.getFirstName() != null) preRegister.setFirstName(request.getFirstName());
        if (request.getLastName() != null) preRegister.setLastName(request.getLastName());
        if (request.getEmail() != null) {
            if (repository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already registered");
            }
            if (accountService.doesAccountExistByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already registered");
            }
            preRegister.setEmail(request.getEmail());
            mailUpdated = true;
        }
        if (request.getRoleName() != null) {
            var roles = Stream.of(Arrays.toString(RoleType.values())).collect(Collectors.toSet());
            if (roles.contains(request.getRoleName().toUpperCase()))
                preRegister.setRoleName(request.getRoleName());
            else
                throw new IllegalArgumentException("No such role");
        }

        repository.save(preRegister);
        if (mailUpdated) {
            try {
                sendMail(request.getEmail(), request.getFirstName(), request.getLastName(), uuid.toString());
            } catch (MessagingException | IOException e) {
                System.out.println("Failed sending mail: " + e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return mapper.TO_RESPONSE(preRegister);
    }

    @Transactional
    public void Delete(UUID uuid) {
        var preRegister = repository.findById(uuid);
        preRegister.ifPresent(repository::delete);
    }
}
