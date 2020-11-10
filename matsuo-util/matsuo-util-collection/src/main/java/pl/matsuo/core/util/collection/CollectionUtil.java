package pl.matsuo.core.util.collection;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;
import static pl.matsuo.core.util.collection.Pair.pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/** Helper methods for collection operations */
public class CollectionUtil {

  public static <F, T> List<T> map(Collection<? extends F> collection, Function<F, T> mapper) {
    List<T> resultList = new ArrayList<>(collection.size());

    for (F element : collection) {
      resultList.add(mapper.apply(element));
    }

    return resultList;
  }

  public static <E> List<E> filter(Collection<E> collection, Predicate<E> condition) {
    List<E> resultList = new ArrayList<>(collection.size());

    for (E element : collection) {
      if (condition.test(element)) {
        resultList.add(element);
      }
    }

    return resultList;
  }

  public static <E, F> List<F> filterMap(Collection<E> collection, Function<E, F> mapper) {
    List<F> resultList = new ArrayList<>();

    for (E element : collection) {
      F mapped = mapper.apply(element);
      if (mapped != null) {
        resultList.add(mapped);
      }
    }

    return resultList;
  }

  public static <E, F> List<F> filterMap(
      Collection<E> collection, Predicate<E> condition, Function<E, F> mapper) {
    List<F> resultList = new ArrayList<>();

    for (E element : collection) {
      if (condition.test(element)) {
        resultList.add(mapper.apply(element));
      }
    }

    return resultList;
  }

  /** Create map from collection. */
  public static <E, F, G> Map<F, G> toMap(
      Collection<E> collection, Function<E, F> keyMapper, Function<E, G> valueMapper) {
    Map<F, G> resultMap = new HashMap<>();
    for (E element : collection) {
      resultMap.put(keyMapper.apply(element), valueMapper.apply(element));
    }

    return resultMap;
  }

  public static <E> E last(List<E> list) {
    return list.get(list.size() - 1);
  }

  public static <E> List<E> merge(Collection<Collection<E>> collections) {
    return flatMap(collections, identity());
  }

  public static <E> List<E> removeNulls(Collection<E> collection) {
    return filterMap(collection, identity());
  }

  public static Map<String, String> stringMap(String... keyValues) {
    Map<String, String> map = new HashMap<>();

    for (int i = 0; i < (keyValues.length / 2); i++) {
      map.put(keyValues[2 * i], keyValues[2 * i + 1]);
    }

    return map;
  }

  /** Reduce <code>list</code> elements. */
  public static <F, T> T fold(
      Collection<? extends F> list, final T startValue, BiFunction<T, F, T> reducer) {
    T value = startValue;

    for (F item : list) {
      value = reducer.apply(value, item);
    }
    return value;
  }

  public static <F, T> List<T> flatMap(
      Collection<? extends F> collection, Function<F, Collection<T>> mapper) {
    List<T> resultList = new ArrayList<>(collection.size());

    for (F element : collection) {
      resultList.addAll(mapper.apply(element));
    }

    return resultList;
  }

  public static <E> List<Pair<Integer, E>> indexed(Collection<E> collection) {
    int index = 0;
    List<Pair<Integer, E>> result = new ArrayList<>(collection.size());

    for (E e : collection) {
      result.add(pair(index, e));
      index++;
    }

    return result;
  }

  public static List<Integer> range(final Integer start, final Integer end) {
    List<Integer> range = new ArrayList<>();

    for (int i = start; i < end; i++) {
      range.add(i);
    }

    return range;
  }

  public static <E> E getFirst(Collection<E> collection, Predicate<E> condition) {
    for (E element : collection) {
      if (condition.test(element)) {
        return element;
      }
    }

    return null;
  }

  public static <E> Optional<E> findFirst(Collection<E> collection, Predicate<E> condition) {
    for (E element : collection) {
      if (condition.test(element)) {
        return of(element);
      }
    }

    return empty();
  }

  public static <E> boolean anyMatch(Collection<E> collection, Predicate<E> condition) {
    for (E element : collection) {
      if (condition.test(element)) {
        return true;
      }
    }

    return false;
  }

  public static <E> boolean allMatch(Collection<E> collection, Predicate<E> condition) {
    for (E element : collection) {
      if (!condition.test(element)) {
        return false;
      }
    }

    return true;
  }

  public static <E> boolean noneMatch(Collection<E> collection, Predicate<E> condition) {
    for (E element : collection) {
      if (condition.test(element)) {
        return false;
      }
    }

    return true;
  }
}
