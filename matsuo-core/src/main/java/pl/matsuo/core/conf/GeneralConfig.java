package pl.matsuo.core.conf;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@Import({GenericConversionService.class, LocalValidatorFactoryBean.class})
@PropertySource("classpath:/app.properties")
public class GeneralConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public MessageSource modelMessageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/i18n-model");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
