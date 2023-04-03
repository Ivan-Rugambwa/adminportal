package almroth.kim.gamendo_user_api;

import almroth.kim.gamendo_user_api.config.NotionConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(NotionConfigProperties.class)
@EnableAsync
public class GamendoUserApi {

    public static void main(String[] args) {
        SpringApplication.run(GamendoUserApi.class, args);
    }

}
