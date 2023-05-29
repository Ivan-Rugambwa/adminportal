package almroth.kim.seat_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("notion")
public record NotionConfigProperties(
        String adminEmail,
        String adminFirstName,
        String adminLastName,
        String secret,
        String aes_key,
        String smtp_auth_email,
        String smtp_auth_password,
        String registerUrl,
        String resetPasswordUrl,
        List<String> allowedCorsUrls,
        boolean enableSwagger,
        boolean smtp_auth,
        String smtp_starttls_enable,
        String smtp_host,
        String smtp_port,
        String mail_from
) {
}