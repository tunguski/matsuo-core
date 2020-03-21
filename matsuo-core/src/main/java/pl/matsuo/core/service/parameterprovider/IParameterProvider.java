package pl.matsuo.core.service.parameterprovider;

/**
 * Interfejs dla fasad dostępu do wartości.
 *
 * @author Marek Romanowski
 * @since Aug 24, 2013
 */
public interface IParameterProvider<U> {

  default <E> E get(String key) {
    return (E) get(key, Object.class);
  };

  <E> E get(String key, Class<E> expectedClass);

  void set(String key, Object value);

  U getUnderlyingEntity();
}
