package pl.matsuo.core.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.matsuo.core.service.mail.IMailService;

import static org.mockito.Mockito.*;

/**
 * Created by marek on 24.01.15.
 */
@Configuration
public class TestMailConfig {


  @Bean
  IMailService mailService() {
    return mock(IMailService.class);
  }
}
