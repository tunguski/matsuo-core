package pl.matsuo.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.matsuo.core.service.execution.ExecutionServiceImpl;

@Configuration
@Import(ExecutionServiceImpl.class)
public class TestDataExecutionConfig {

  @Autowired(required = true)
  protected ExecutionServiceImpl executionService;

  @Bean
  public static TestDataDiscoveryRegisteringBeanFactoryPostProcessor
      testDataDiscoveryRegisteringBeanFactoryPostProcessor() {
    return new TestDataDiscoveryRegisteringBeanFactoryPostProcessor();
  }
}
