package pl.matsuo.core.service.facade;

import java.util.function.Consumer;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;

/**
 * Interfejs budowania fasad dla różnych typów obiektów.
 *
 * @since Aug 25, 2013
 */
public interface IFacadeBuilder {

  <E> E createFacade(Object object, Class<E> clazz);

  <E> E createFacade(Object object, Class<E> clazz, String prefix);

  <E> E createFacade(IFacadeAware facadeAware);

  default <E> void doWithFacade(Object object, Class<E> clazz, Consumer<E> callable) {
    callable.accept(createFacade(object, clazz));
  }

  default <E> void doWithFacade(IFacadeAware facadeAware, Consumer<E> callable) {
    callable.accept(createFacade(facadeAware));
  }

  IParameterProvider<?> createParameterProvider(Object object);
}
