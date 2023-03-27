package almroth.kim.gamendo_user_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("notion")
public record NotionConfigProperties(
        String secret
) {
}