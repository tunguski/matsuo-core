package pl.matsuo.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.matsuo.core.service.execution.ExecutionServiceImpl;

@Configuration
public class TestDataExecutionConfig {

  @Autowired(required = true)
  protected ExecutionServiceImpl executionService;

  @Bean
  public static TestDataDiscoveryRegisteringBeanFactoryPostProcessor
      testDataDiscoveryRegisteringBeanFactoryPostProcessor() {
    return new TestDataDiscoveryRegisteringBeanFactoryPostProcessor();
  }

  @Bean
  public static BeanFactoryPostProcessor executionService() {
    return new ClassesAddingBeanFactoryPostProcessor(ExecutionServiceImpl.class);
  }
}
