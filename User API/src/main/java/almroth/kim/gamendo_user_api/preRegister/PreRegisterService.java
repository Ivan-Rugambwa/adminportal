package almroth.kim.gamendo_user_api.preRegister;

import almroth.kim.gamendo_user_api.account.AccountService;
import almroth.kim.gamendo_user_api.business.BusinessService;
import almroth.kim.gamendo_user_api.config.JwtService;
import almroth.kim.gamendo_user_api.config.NotionConfigProperties;
import almroth.kim.gamendo_user_api.mapper.PreRegisterMapper;
import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterRequest;
import almroth.kim.gamendo_user_api.preRegister.dto.PreRegisterResponse;
import almroth.kim.gamendo_user_api.preRegister.model.PreRegister;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final NotionConfigProperties env;

    private final PreRegisterMapper mapper = Mappers.getMapper(PreRegisterMapper.class);

    public Set<PreRegisterResponse> GetAllPreRegisters(){
        var preRegisters = repository.findAll();
        Set<PreRegisterResponse> preRegisterResponses = new HashSet<>();

        for (var preRegister :
                preRegisters) {
            preRegisterResponses.add(mapper.TO_RESPONSE(preRegister));
        }
        return preRegisterResponses;
    }
    public PreRegisterResponse GetByUuid(UUID uuid){
        var preRegister = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        return mapper.TO_RESPONSE(preRegister);
    }

    @Transactional
    public PreRegisterResponse Create(PreRegisterRequest request){

        if (repository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already registered");
        }
        if (accountService.doesAccountExistByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        var preRegister = PreRegister.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .businessName(request.getBusinessName())
                .build();

        repository.save(preRegister);
        try {
            sendMail(request.getEmail(), preRegister.getUuid().toString());
        } catch (MessagingException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return mapper.TO_RESPONSE(preRegister);
    }
    @Transactional
    public void DeleteByUuid(UUID uuid) {
        var preRegister = repository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        repository.delete(preRegister);
    }

    public void sendMail(String toEmail, String uuid) throws MessagingException {
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
                Message.RecipientType.TO, InternetAddress.parse("kim.almroth@apendo.se"));
        message.setSubject("Mail Subject");

        String msg = "Du har blivit tillfrågad att skapa ett konto på apendo.seat,\nVänligen tryck på denna länk för att skapa ditt konto: " + env.registerUrl() + "?uuid=" + uuid;

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }

    @Transactional
    public PreRegisterResponse Update(PreRegisterRequest request, UUID uuid) {
        var mailUpdated = false;
        var preRegister = repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("No such preregister"));
        if (request.getBusinessName() != null) preRegister.setBusinessName(request.getBusinessName());
        if (request.getFirstName() != null) preRegister.setFirstName(request.getFirstName());
        if (request.getLastName() != null) preRegister.setLastName(request.getLastName());
        if (request.getEmail() != null) {
            if (repository.findByEmail(request.getEmail()).isPresent()){
                throw new IllegalArgumentException("Email already registered");
            }
            if (accountService.doesAccountExistByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already registered");
            }
            preRegister.setEmail(request.getEmail());
            mailUpdated = true;
        }

        repository.save(preRegister);
        if (mailUpdated){
            try {
                sendMail(request.getEmail(), uuid.toString());
            } catch (MessagingException e) {
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
