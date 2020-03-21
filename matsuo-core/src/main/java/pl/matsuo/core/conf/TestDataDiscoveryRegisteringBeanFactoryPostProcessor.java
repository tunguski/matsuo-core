package pl.matsuo.core.conf;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/** Created by tunguski on 06.10.13. */
public class TestDataDiscoveryRegisteringBeanFactoryPostProcessor
    implements BeanFactoryPostProcessor {

  public void discover(Class<?> clazz, Set<Class> toAdd) {
    DiscoverTypes annotation = clazz.getAnnotation(DiscoverTypes.class);
    if (annotation != null) {
      toAdd.addAll(Arrays.asList(annotation.value()));
      for (Class nextClass : annotation.value()) {
        discover(nextClass, toAdd);
      }
    }
  }

  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    try {
      BeanDefinitionRegistry factory = (BeanDefinitionRegistry) beanFactory;
      Set<Class> toAdd = new HashSet<>();

      // odnajduje wszystkie referencje opisane przez Discover
      for (String beanName : beanFactory.getBeanDefinitionNames()) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        if (beanDefinition.getBeanClassName() != null) {
          discover(Class.forName(beanDefinition.getBeanClassName()), toAdd);
        }
      }

      // usuwa ze zbioru te elementy, które już są w kontekście
      for (String beanName : beanFactory.getBeanDefinitionNames()) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        if (beanDefinition.getBeanClassName() != null) {
          toAdd.remove(Class.forName(beanDefinition.getBeanClassName()));
        }
      }

      // tworzy nowe definicje beanów dla brakujących elementów
      for (Class clazz : toAdd) {
        factory.registerBeanDefinition(
            clazz.getSimpleName(), rootBeanDefinition(clazz).getBeanDefinition());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
