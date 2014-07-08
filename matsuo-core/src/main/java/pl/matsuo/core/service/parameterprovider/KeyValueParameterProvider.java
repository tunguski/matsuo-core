package pl.matsuo.core.service.parameterprovider;

import pl.matsuo.core.model.kv.KeyValueEntity;

import java.math.BigDecimal;
import java.util.Date;

import static pl.matsuo.core.util.ReflectUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


/**
 * Provider dla obiektów {@link KeyValueEntity}.
 * @author Marek Romanowski
 * @since Aug 24, 2013
 */
public class KeyValueParameterProvider extends AbstractParameterProvider<KeyValueEntity> {


  public KeyValueParameterProvider(KeyValueEntity underlyingEntity) {
    super(underlyingEntity);
  }


  @Override
  public Object internalGet(String key, Class<?> expectedClass) {
    return optional((Object) underlyingEntity.getFields().get(key)).orElseGet(
        () -> processEx(() -> { return getValue(underlyingEntity, key); }, e -> { return null; }));
  }


  @Override
  public void set(String propertyName, Object value) {
    if (value == null) {
      underlyingEntity.getFields().put(propertyName, null);
    } else {
      Class<?> parameterType = value.getClass();
      if (parameterType.equals(Integer.class)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else if (parameterType.equals(Double.class)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else if (parameterType.equals(String.class)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else if (parameterType.equals(Date.class)) {
        underlyingEntity.getFields().put(propertyName, dateFormat.format(value));
      } else if (parameterType.equals(BigDecimal.class)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else if (parameterType.equals(Boolean.class)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else if (Enum.class.isAssignableFrom(parameterType)) {
        underlyingEntity.getFields().put(propertyName, value.toString());
      } else {
        throw new IllegalArgumentException("Unknown property type: " + parameterType.getName());
      }
    }
  }
}

