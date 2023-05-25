package almroth.kim.seat_api.passwordReset;

import almroth.kim.seat_api.account.model.Account;
import almroth.kim.seat_api.config.NotionConfigProperties;
import almroth.kim.seat_api.passwordReset.model.PasswordReset;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;

    private final NotionConfigProperties env;

    public PasswordResetService(PasswordResetRepository passwordResetRepository, NotionConfigProperties env) {
        this.passwordResetRepository = passwordResetRepository;
        this.env = env;
    }

    public PasswordReset createPasswordReset(Account account) {
        var passwordResetOld = passwordResetRepository.findByAccount_Uuid(account.getUuid());
        passwordResetOld.ifPresent(passwordResetRepository::delete);

        var passwordReset = PasswordReset.builder()
                .createdAtDate(Date.from(Instant.now()))
                .account(account)
                .build();

        passwordResetRepository.save(passwordReset);
        return passwordReset;
    }

    public void sendPasswordResetEmail(String to, String uuid, String firstName) throws MessagingException, IOException {
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
                Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Återställning av lösenord");

        var passwordResetUrl = env.resetPasswordUrl() + "?uuid=" + uuid;

        var path = Path.of("../Email/password-reset.html");
        String email = new String(Files.readAllBytes(path));
        email = email.replaceAll(Pattern.quote("{firstName}"), firstName);
        email = email.replaceAll(Pattern.quote("{passwordResetUrl}"), passwordResetUrl);


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(email, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public boolean doesResetPasswordExist(UUID resetPasswordUuid) {
        var passwordResetOptional = passwordResetRepository.getByUuid(resetPasswordUuid);
        if (passwordResetOptional.isEmpty())
            return false;
        var passwordReset = passwordResetOptional.get();
        var datePlus = passwordReset.getCreatedAtDate().toInstant().plus(Duration.ofMinutes(30));
        if (Instant.now().isAfter(datePlus)) {
            deletePasswordResetByUuid(passwordReset.getUuid());
            return false;
        } else
            return true;
    }

    public PasswordReset getPasswordResetByUuid(UUID resetPasswordUuid) {
        return passwordResetRepository.getByUuid(resetPasswordUuid).orElseThrow(() -> new IllegalArgumentException("No such password reset"));
    }

    public void deletePasswordResetByUuid(UUID uuid) {
        passwordResetRepository.delete(passwordResetRepository.getByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("No such password reset")));
    }
}
