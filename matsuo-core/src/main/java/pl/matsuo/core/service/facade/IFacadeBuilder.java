package pl.matsuo.core.service.facade;


import pl.matsuo.core.service.parameterprovider.IParameterProvider;

import java.util.function.Consumer;

/**
 * Interfejs budowania fasad dla różnych typów obiektów.
 * @author Marek Romanowski
 * @since Aug 25, 2013
 */
public interface IFacadeBuilder {


  <E> E createFacade(Object object, Class<E> clazz);


  <E> E createFacade(Object object, Class<E> clazz, String prefix);


  <E> E createFacade(IFacadeAware facadeAware);

  default <E> void doWithFacade(Object object, Class<E> clazz, Consumer<E> callable) {
    callable.accept(createFacade(object, clazz));
  };

  default <E> void doWithFacade(IFacadeAware facadeAware, Consumer<E> callable) {
    callable.accept(createFacade(facadeAware));
  };

  IParameterProvider<?> createParameterProvider(Object object);
}

