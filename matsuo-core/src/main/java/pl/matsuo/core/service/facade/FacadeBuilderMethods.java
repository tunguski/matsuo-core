package pl.matsuo.core.service.facade;

/**
 * Interfejs budowania fasad dla różnych typów obiektów.
 *
 * @since Aug 25, 2013
 */
public interface FacadeBuilderMethods extends IFacadeBuilder {

  IFacadeBuilder getFacadeBuilder();

  @Override
  default <E> E createFacade(Object object, Class<E> clazz) {
    return getFacadeBuilder().createFacade(object, clazz);
  };

  default <E> E createFacade(Object object, Class<E> clazz, String prefix) {
    return getFacadeBuilder().createFacade(object, clazz, prefix);
  };

  default <E> E createFacade(IFacadeAware facadeAware) {
    return getFacadeBuilder().createFacade(facadeAware);
  };
}
