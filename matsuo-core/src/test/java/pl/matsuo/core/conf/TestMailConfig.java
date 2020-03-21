package pl.matsuo.core.conf;

import static org.mockito.Mockito.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.matsuo.core.service.mail.IMailService;

/** Created by marek on 24.01.15. */
@Configuration
public class TestMailConfig {

  @Bean
  IMailService mailService() {
    return mock(IMailService.class);
  }
}
