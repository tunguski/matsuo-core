package pl.matsuo.core.service.parameterprovider;

import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

/**
 * Nadklasa dla wszystkich providerów.
 *
 * @author Marek Romanowski
 * @since Aug 24, 2013
 */
public abstract class AbstractParameterProvider<U> implements IParameterProvider<U> {

  protected final DateFormat dateFormat = new ISO8601DateFormat();

  protected final U underlyingEntity;

  public AbstractParameterProvider(U underlyingEntity) {
    this.underlyingEntity = underlyingEntity;
  }

  @Override
  public final <E> E get(String key, Class<E> expectedClass) {
    Object value = internalGet(key, expectedClass);

    if (value == null) {
      return (E) value;
    }

    if (Number.class.isAssignableFrom(expectedClass)
        && Number.class.isAssignableFrom(value.getClass())) {
      Number number = (Number) value;
      if (expectedClass.equals(Integer.class)) {
        return (E) (Integer) number.intValue();
      } else if (expectedClass.equals(BigDecimal.class)) {
        return (E) bd(number.toString());
      }
    }

    if (expectedClass.equals(String.class) || !value.getClass().equals(String.class)) {
      return (E) value;
    }

    String stringValue = (String) value;

    if (Enum.class.isAssignableFrom(expectedClass)) {
      return (E) Enum.valueOf((Class<Enum>) expectedClass, stringValue);
    } else if (expectedClass.equals(Integer.class)) {
      return (E) (Integer) bd(stringValue).intValue();
    } else if (expectedClass.equals(Date.class)) {
      return (E) date(stringValue);
    } else if (expectedClass.equals(BigDecimal.class)) {
      return (E) bd(stringValue);
    } else if (expectedClass.equals(Boolean.class)) {
      return (E) Boolean.valueOf(stringValue);
    }

    if (Object.class.equals(expectedClass)) {
      return (E) value;
    }

    return null;
  }

  public abstract Object internalGet(String key, Class<?> expectedClass);

  @Override
  public U getUnderlyingEntity() {
    return underlyingEntity;
  }

  public AbstractParameterProvider<U> buildParameterProvider(U object) {
    try {
      return getClass().getConstructor(object.getClass()).newInstance(object);
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate parameterProvider " + getClass().getName());
    }
  }
}
