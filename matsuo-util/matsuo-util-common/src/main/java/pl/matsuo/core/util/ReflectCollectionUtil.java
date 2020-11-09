package pl.matsuo.core.util;

import static pl.matsuo.core.util.ReflectUtil.getValue;
import static pl.matsuo.core.util.collection.CollectionUtil.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Helper methods for collection operations */
public class ReflectCollectionUtil {

  /**
   * Creates new list by collecting value of property <code>property</code> from all elements of
   * <code>collection</code>.
   */
  public static <E> List<E> collect(Collection<?> collection, String property) {
    return map(collection, object -> getValue(object, property));
  }

  /**
   * Create map where keys are values of <code>property</code> from <code>collection</code>
   * elements.
   */
  public static <E, F> Map<E, F> toMap(Collection<F> collection, String property) {
    Map<E, F> resultMap = new HashMap<>();
    for (F object : collection) {
      resultMap.put((E) getValue(object, property), object);
    }

    return resultMap;
  }
}
