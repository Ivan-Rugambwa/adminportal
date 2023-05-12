package almroth.kim.gamendo_user_api.passwordReset;

import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.config.NotionConfigProperties;
import almroth.kim.gamendo_user_api.error.customException.PasswordResetTimeoutException;
import almroth.kim.gamendo_user_api.passwordReset.model.PasswordReset;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

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

    public void sendPasswordResetEmail(String to, String uuid) throws MessagingException {
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
                Message.RecipientType.TO, InternetAddress.parse("kim.almroth@apendo.se, ivan.rugambwa@apendo.se"));
        message.setSubject("Mail Subject");

        String msg = "Du har förfrågat att återställa ditt lösenord på http://camcaas.apendo.se, tryck på denna länk för att skapa ett nytt lösenord: " +
                env.resetPasswordUrl() + "?uuid=" + uuid +
                "\n\nDenna länk är giltig i 30 minuter.";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

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
