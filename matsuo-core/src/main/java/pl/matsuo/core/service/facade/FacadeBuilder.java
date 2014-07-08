package pl.matsuo.core.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;
import pl.matsuo.core.service.parameterprovider.AbstractParameterProvider;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Class.*;
import static java.lang.reflect.Proxy.*;
import static org.springframework.core.GenericTypeResolver.*;
import static org.springframework.util.Assert.*;


@Service
public class FacadeBuilder implements IFacadeBuilder {
  private static final Logger logger = LoggerFactory.getLogger(FacadeBuilder.class);


  protected Map<Class, Class<? extends AbstractParameterProvider>> parameterProviders;


  @Override
  public <E> E createFacade(IFacadeAware facadeAware) {
    return (E) createFacade(facadeAware, facadeAware.getPrintFacadeClass());
  }


  @Override
  public <E> E createFacade(Object object, Class<E> clazz) {
    return createFacade(object, clazz, "");
  }


  @Override
  public <E> E createFacade(Object object, Class<E> clazz, String prefix) {
    notNull(object, "Facade underlying object must not be null");
    notNull(clazz, "Facade class must not be null");
    notNull(prefix, "Prefix must not be null");

    return (E) newProxyInstance(
        getClass().getClassLoader(),
        new Class[] { clazz },
        new FacadeInvocationHandler(
            object instanceof IParameterProvider ? (IParameterProvider) object : createParameterProvider(object),
            this, clazz, getClass().getClassLoader(), prefix));
  }


  @Override
  public IParameterProvider createParameterProvider(Object object) {
    if (parameterProviders == null) {
      initializeProviders();
    }

    for (Class clazz : parameterProviders.keySet()) {
      if (clazz.isAssignableFrom(object.getClass())) {
        try {
          for (Constructor constructor : parameterProviders.get(clazz).getConstructors()) {
            if (constructor.getParameterTypes().length == 1
                && constructor.getParameterTypes()[0].isAssignableFrom(object.getClass())) {
              return (IParameterProvider) constructor.newInstance(object);
            }
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    throw new IllegalArgumentException("ParameterProvider not found for class " + object.getClass());
  }


  public void initializeProviders() {
    parameterProviders = new HashMap<>();

    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractParameterProvider.class));

    // scan in org.example.package
    Set<BeanDefinition> components = provider.findCandidateComponents("pl/matsuo");
    for (BeanDefinition component : components) {
      try {
        Class<?> clazz = forName(component.getBeanClassName());

        if (!AbstractParameterProvider.class.isAssignableFrom(clazz)) {
          continue;
        }

        Class<?> baseObjectType = resolveTypeArgument(clazz, IParameterProvider.class);
        if (baseObjectType == null) {
          logger.warn("Could not resolve type argument for class: " + clazz.getName());
        } else {
          parameterProviders.put(baseObjectType, (Class<? extends AbstractParameterProvider>) clazz);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}

