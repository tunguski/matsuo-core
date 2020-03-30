package pl.matsuo.core.conf;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.matsuo.core.service.mail.IMailService;

@Configuration
public class TestMailConfig {

  @Bean
  IMailService mailService() {
    return mock(IMailService.class);
  }
}
