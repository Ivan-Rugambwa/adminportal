package almroth.kim.seat_api;

import almroth.kim.seat_api.config.NotionConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(NotionConfigProperties.class)
@EnableAsync
@EnableWebMvc
public class ApendoSeatUserApi {

    public static void main(String[] args) {
        SpringApplication.run(ApendoSeatUserApi.class, args);
    }

}
