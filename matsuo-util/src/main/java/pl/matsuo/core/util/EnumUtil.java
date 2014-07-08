package pl.matsuo.core.util;


public class EnumUtil {


  public static <E extends Enum> E enumValue(Class<E> clazz, String value) {
    if (value == null) {
      return null;
    } else {
      for (E constant : clazz.getEnumConstants()) {
        if (constant.name().equals(value)) {
          return constant;
        }
      }
    }

    throw new IllegalArgumentException("No enum constant " + value + " in enum " + clazz.getName());
  }
}

