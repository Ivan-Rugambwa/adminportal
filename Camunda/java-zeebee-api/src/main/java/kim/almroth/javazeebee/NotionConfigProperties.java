package kim.almroth.javazeebee;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("notion")
public record NotionConfigProperties(
        List<String> allowedCorsUrls

) {
}
