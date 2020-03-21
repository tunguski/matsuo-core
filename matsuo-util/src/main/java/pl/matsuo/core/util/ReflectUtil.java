package pl.matsuo.core.util;

import static com.google.common.collect.Lists.*;
import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.*;

import com.google.common.base.Joiner;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ResolvableType;
import pl.matsuo.core.util.function.Failure;
import pl.matsuo.core.util.function.Success;
import pl.matsuo.core.util.function.Try;

@SuppressWarnings("unchecked")
public class ReflectUtil {

  public static <X> Class<X> resolveType(Class<?> clazz, Class<?> superClazz, int index) {
    return (Class<X>) ResolvableType.forClass(clazz).as(superClazz).resolveGeneric(index);
  }

  interface EFunction<T, R> {
    R apply(T t) throws Exception;
  }

  interface EBiFunction<T, U, R> {
    R apply(T t, U u) throws Exception;
  }

  private static <E extends AccessibleObject> Try<Object> accessMember(
      Object object,
      String fieldName,
      EFunction<String, E> memberGetter,
      EBiFunction<E, Object, Object> valueGetter) {
    try {
      E member = memberGetter.apply(fieldName);
      try {
        member.setAccessible(true);
        return new Success<>(valueGetter.apply(member, object));
      } finally {
        member.setAccessible(false);
      }
    } catch (Exception e) {
    }

    return new Failure<>();
  }

  protected static <E> Try<Object> getValue(Object object, String fieldName, Class clazz) {
    if (!clazz.equals(Object.class)) {
      return ReflectUtil.<Field>accessMember(object, fieldName, clazz::getDeclaredField, Field::get)
          .ifFailure(
              () ->
                  ReflectUtil.<Method>accessMember(
                      object,
                      "get" + capitalize(fieldName),
                      clazz::getDeclaredMethod,
                      Method::invoke))
          .ifFailure(() -> getValue(object, fieldName, clazz.getSuperclass()));
    }

    throw new RuntimeException("No such property/getter in class ");
  }

  public static <E> E getValue(Object object, String fieldName) {
    return (E) getValue(object, fieldName, object.getClass()).get();
  }

  /**
   * Pobiera typ pojedynczego pola. Analizuje listę przekazanych klas do skutku. Przydatne przy
   * przeszukiwaniu interfejsów rozszerzanych przez inny interfejs. Wtedy nie mamy implementacji
   * metody w "trzymanej" klasie, jedynie listę interfejsów w których należy szukać.
   */
  protected static <E> Class<E> getExactPropertyType(List<Class> classes, String exactFieldName) {
    for (Class clazz : classes) {
      Class<E> propertyType = getExactPropertyType(clazz, exactFieldName);
      if (propertyType != null) {
        return propertyType;
      }
    }

    return null;
  }

  /** Pobiera typ pojedynczego pola. */
  protected static <E> Class<E> getExactPropertyType(final Class<?> clazz, String exactFieldName) {
    if (clazz == null || clazz.equals(Object.class)) {
      return null;
    }

    // próba pobrania typu z pola
    try {
      return (Class<E>) clazz.getDeclaredField(exactFieldName).getType();
    } catch (Exception e) {
    }

    // próba pobrania typu z gettera
    try {
      return (Class<E>) clazz.getDeclaredMethod("get" + capitalize(exactFieldName)).getReturnType();
    } catch (Exception e) {
    }

    // tworzymy listę nadklas i nadinterfejsów
    List<Class> classes = new ArrayList<>();
    if (clazz.getSuperclass() != null) {
      classes.add(clazz.getSuperclass());
    }
    classes.addAll(asList(clazz.getInterfaces()));

    // wywołujemy
    return getExactPropertyType(classes, exactFieldName);
  }

  /** Pobiera typ pola, nawet jeśli jest to zagnieżdzona definicja (typu "entity.person.id"). */
  public static <E> Class<E> getPropertyType(final Class<?> clazz, String fieldName) {
    Class<?> exactType = clazz;
    for (String fieldPart : fieldName.split("[.]")) {
      exactType = getExactPropertyType(exactType, fieldPart);

      if (exactType == null) {
        throw new RuntimeException(
            "No such property/getter in class " + clazz.getSimpleName() + " for " + fieldName);
      }
    }

    if (exactType == null) {
      throw new RuntimeException(
          "No such property/getter in class" + clazz.getSimpleName() + " for " + fieldName);
    } else {
      return (Class<E>) exactType;
    }
  }

  /** Pobiera typ pojedynczego pola. */
  protected static AnnotatedElement getExactAnnotatedElement(
      List<Class> classes, String exactFieldName) {
    for (Class clazz : classes) {
      AnnotatedElement annotatedElement = getExactAnnotatedElement(clazz, exactFieldName);
      if (annotatedElement != null) {
        return annotatedElement;
      }
    }

    return null;
  }

  /** Pobiera typ pojedynczego pola. */
  protected static AnnotatedElement getExactAnnotatedElement(
      final Class<?> clazz, String exactFieldName) {
    if (clazz == null || clazz.equals(Object.class)) {
      return null;
    }

    // próba pobrania typu z pola
    try {
      return clazz.getDeclaredField(exactFieldName);
    } catch (Exception e) {
    }

    try {
      return clazz.getDeclaredMethod("get" + capitalize(exactFieldName));
    } catch (Exception e) {
    }

    // tworzymy listę nadklas i nadinterfejsów
    List<Class> classes = new ArrayList<>();
    if (clazz.getSuperclass() != null) {
      classes.add(clazz.getSuperclass());
    }
    classes.addAll(asList(clazz.getInterfaces()));

    // wywołujemy
    return getExactAnnotatedElement(classes, exactFieldName);
  }

  public static AnnotatedElement getAnnotatedElement(Class<?> clazz, String fieldName) {
    String[] splitted = fieldName.split("[.]");
    List<String> prefix = newArrayList(splitted);
    String lastElement = prefix.remove(prefix.size() - 1);

    if (!prefix.isEmpty()) {
      clazz = getPropertyType(clazz, Joiner.on(".").join(prefix));
    }

    return getExactAnnotatedElement(clazz, lastElement);
  }

  public static <E> E invoke(Object target, String methodName, Object... args) {
    for (Method method : target.getClass().getMethods()) {
      if (method.getName().equals(methodName)) {
        try {
          return (E) method.invoke(target, args);
        } catch (Exception e) {
          throw new RuntimeException("Exception invoking method " + methodName, e);
        }
      }
    }

    throw new RuntimeException("Method " + methodName + " not found");
  }

  public static String fieldName(String methodName) {
    return uncapitalize(methodName.substring(3));
  }
}
