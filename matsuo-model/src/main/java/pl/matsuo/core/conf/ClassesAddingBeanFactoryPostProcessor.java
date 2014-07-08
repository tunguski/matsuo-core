package pl.matsuo.core.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.*;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.*;


/**
 * Created by tunguski on 06.10.13.
 */
public class ClassesAddingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


  private Class[] classes;


  public ClassesAddingBeanFactoryPostProcessor(Class ... classes) {
    this.classes = classes;
  }


  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    BeanDefinitionRegistry factory = (BeanDefinitionRegistry) beanFactory;

    Set<Class> toAdd = new HashSet<>(asList(classes));

    // usuwa ze zbioru te elementy, które już są w kontekście
    for (String beanName : beanFactory.getBeanDefinitionNames()) {
      BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
      if (beanDefinition.getBeanClassName() != null) {
        try {
          toAdd.remove(Class.forName(beanDefinition.getBeanClassName()));
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }

    for (Class clazz : toAdd) {
      factory.registerBeanDefinition(clazz.getSimpleName(), rootBeanDefinition(clazz).getBeanDefinition());
    }
  }
}
