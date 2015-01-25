package pl.matsuo.core.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


/**
 * Created by marek on 24.01.15.
 */
@Configuration
public class MailConfig {


  @Bean
  public JavaMailSender mailSender() {
    return new JavaMailSenderImpl();
  }
}

