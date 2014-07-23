package pl.matsuo.core.service.facade;

import org.springframework.util.ClassUtils;
import pl.matsuo.core.model.kv.IKeyValueFacade;
import pl.matsuo.core.model.kv.KeyValueEntity;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;
import pl.matsuo.core.service.parameterprovider.KeyValueParameterProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.core.GenericTypeResolver.*;
import static org.springframework.core.GenericTypeResolver.resolveType;
import static pl.matsuo.core.util.ReflectUtil.*;


public class FacadeInvocationHandler<E> implements InvocationHandler {


  protected final IParameterProvider<?> parameterProvider;
  protected final FacadeBuilder facadeBuilder;
  protected final Class<E> clazz;
  protected final ClassLoader classLoader;
  protected final String prefix;


  public FacadeInvocationHandler(IParameterProvider<?> parameterProvider, FacadeBuilder facadeBuilder,
                                 Class<E> clazz, ClassLoader classLoader, String prefix) {
    this.parameterProvider = parameterProvider;
    this.facadeBuilder = facadeBuilder;
    this.clazz = clazz;
    this.classLoader = classLoader;
    this.prefix = prefix;
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();
    if (name.startsWith("get")
        && !name.equals("getClass")
        && method.getParameterTypes().length == 0) {
      String propertyName = fieldName(name);

      Class<?> returnType = method.getReturnType();

      Object propertyValue = parameterProvider.get(prefix + propertyName, returnType);

      if (IKeyValueFacade.class.isAssignableFrom(returnType)) {
        return facadeBuilder.createFacade(
            propertyValue != null ? propertyValue : parameterProvider,
            returnType,
            propertyName + ".");
      } else if (propertyValue == null) {
        // jeśli brak wartości, zwracamy null
        return null;
      } else if (!Collection.class.isAssignableFrom(returnType)) {
        // poniższe nie mają zastosowania, jeśli oczekiwanym typem jest kolekcja

        if (returnType.isAssignableFrom(propertyValue.getClass())) {
          // jeśli typ wartości zgadza się ze zwracanym typem, przekazujemy wartosć bezpośrednio
          return propertyValue;
        } else if (!propertyValue.getClass().equals(String.class)) {
          throw new RuntimeException("Cannot transform from " + propertyValue.getClass()
              + " to " + returnType.getClass() + ". Only Strings may be converted");
        } else if (propertyValue.toString().trim().isEmpty()) {
          // jeśli wartość w postaci napisu jest pusta, nie możemy transformować jej do obiektu
          return null;
        }
      }

      if (Collection.class.isAssignableFrom(returnType)) {
        if (returnType.equals(List.class)) {
          final ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
          Type genericType = genericReturnType.getActualTypeArguments()[0];
          final Class genericClass = extracted(proxy, genericType);

          if (IKeyValueFacade.class.isAssignableFrom(genericClass)) {
            final List<KeyValueEntity> elementsList =
                getValue(parameterProvider.getUnderlyingEntity(), prefix + propertyName);
            final Map<KeyValueEntity, IKeyValueFacade> elements = new HashMap<>();

            return new AbstractList<Object>() {
              @Override
              public Object get(int index) {
                KeyValueEntity subElement = elementsList.get(index);

                if (elements.containsKey(subElement)) {
                  return elements.get(subElement);
                } else {
                  IKeyValueFacade elementFacade = (IKeyValueFacade) facadeBuilder.createFacade(
                      new KeyValueParameterProvider(subElement), genericClass, "");

                  elements.put(subElement, elementFacade);

                  return elementFacade;
                }
              }

              @Override
              public int size() {
                return elementsList.size();
              }
            };
          }
        }
      }

      return propertyValue;
    } else if (name.startsWith("set")
               && method.getReturnType().equals(ClassUtils.forName("void", classLoader))
               && method.getParameterTypes().length == 1) {
      String propertyName = fieldName(name);
      parameterProvider.set(prefix + propertyName, args[0]);

      return null;
    } else if (name.equals("getClass")) {
      return clazz;
    } else {
      return method.invoke(parameterProvider.getUnderlyingEntity(), args);
    }
  }


  private Class extracted(Object proxy, Type genericType) {
    if (genericType instanceof Class) {
      return (Class) genericType;
    } else if (genericType instanceof TypeVariable) {
      return resolveType(genericType, getTypeVariableMap(proxy.getClass()));
    }

    return Object.class;
  }
}

