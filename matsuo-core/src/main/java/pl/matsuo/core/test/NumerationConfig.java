package pl.matsuo.core.test;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.matsuo.core.conf.ClassesAddingBeanFactoryPostProcessor;
import pl.matsuo.core.service.numeration.MonthlyNumerationSchemaStrategy;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.service.numeration.QuaterlyNumerationSchemaStrategy;

/**
 * Created by marek on 23.07.14.
 */
@Configuration
public class NumerationConfig {


  @Bean
  public static BeanFactoryPostProcessor mvcServices() {
    return new ClassesAddingBeanFactoryPostProcessor(NumerationServiceImpl.class,
        MonthlyNumerationSchemaStrategy.class, QuaterlyNumerationSchemaStrategy.class);
  }
}
