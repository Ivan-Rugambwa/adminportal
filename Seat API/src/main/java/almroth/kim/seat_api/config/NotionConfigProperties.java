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
        String email,
        String password,
        String registerUrl,
        String resetPasswordUrl,
        List<String> allowedCorsUrls,
        boolean enableSwagger

) {
}