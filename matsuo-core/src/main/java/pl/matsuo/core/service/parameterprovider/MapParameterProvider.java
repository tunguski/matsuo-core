package pl.matsuo.core.service.parameterprovider;

import java.util.Map;


/**
 * Provider bazujący na mapie elementów.
 * @author Marek Romanowski
 * @since Aug 24, 2013
 */
public class MapParameterProvider extends AbstractParameterProvider<Map<String, Object>> {


  public MapParameterProvider(Map<String, ?> underlyingEntity) {
    super((Map<String, Object>) underlyingEntity);
  }


  @Override
  public Object internalGet(String key, Class<?> expectedClass) {
    return underlyingEntity.get(key);
  }


  @Override
  public void set(String key, Object value) {
    underlyingEntity.put(key, value);
  }
}

