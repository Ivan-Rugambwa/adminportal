package kim.almroth.javazeebee;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableZeebeClient
@EnableConfigurationProperties(NotionConfigProperties.class)

public class JavaZeebeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaZeebeApplication.class, args);
    }

}