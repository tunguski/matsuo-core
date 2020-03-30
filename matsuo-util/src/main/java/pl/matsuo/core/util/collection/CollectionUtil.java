package pl.matsuo.core.util.collection;

import static pl.matsuo.core.util.ReflectUtil.getValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/** Pomocnicze metody przy operowaniu na kolekcjach. */
public class CollectionUtil {

  /**
   * Tworzy nową listę na bazie przekazanej kolekcji, poprzez pobranie wartości <code>property
   * </code>. Powtórzenia identycznych wartości zostają usunięte.
   */
  public static final <E> List<E> collect(Collection<?> collection, String property) {
    return (List<E>) map(collection, (Object object) -> getValue(object, property));
  }

  /**
   * Tworzy kolekcję wartości na podstawie przekazanej listy kolekcji poprzez pobranie wszystkich
   * wartości z przekazanych kolekcji.
   */
  public static final <E> List<E> flatten(Collection<?>... collections) {
    List<E> resultList = new ArrayList<>();

    for (Collection<?> collection : collections) {
      resultList.addAll((Collection<? extends E>) collection);
    }

    return new ArrayList<>(new LinkedHashSet<>(resultList));
  }

  public static final <F, T> List<T> map(
      Collection<? extends F> collection, Function<F, T> mapper) {
    List<T> resultList = new ArrayList<>(collection.size());

    for (F element : collection) {
      resultList.add(mapper.apply(element));
    }

    return resultList;
  }

  public static final <E> List<E> filter(Collection<E> collection, Predicate<E> condition) {
    List<E> resultList = new ArrayList<>(collection.size());

    for (E element : collection) {
      if (condition.test(element)) {
        resultList.add(element);
      }
    }

    return resultList;
  }

  /**
   * Tworzy mapę w której kluczem jest wartość pola <code>property</code> z obiektu kolekcji <code>
   * collection</code>, a wartością jest tenże obiekt.
   */
  public static final <E, F> Map<E, F> toMap(Collection<F> collection, String property) {
    Map<E, F> resultMap = new HashMap<E, F>();
    for (F object : collection) {
      resultMap.put((E) getValue(object, property), object);
    }

    return resultMap;
  }

  public static final Function<Object, String> toStringMapping =
      (Object object) -> object.toString();

  /** Tworzy mapę w której klucze zostają przetworzone */
  public static final <D, E, F> Map<D, F> reMap(
      Map<? extends E, F> sourceMap, Function<E, D> mapping) {
    Map<D, F> resultMap = new HashMap<D, F>();
    for (E key : sourceMap.keySet()) {
      resultMap.put(mapping.apply(key), sourceMap.get(key));
    }

    return resultMap;
  }

  public static <E> E last(List<E> list) {
    return list.get(list.size() - 1);
  }

  public static final <E> List<E> merge(Collection<E>... collections) {
    return flatten(collections);
  }

  public static final <E, F extends Collection<E>> F removeNulls(F collection) {
    Iterator<E> iterator = collection.iterator();
    while (iterator.hasNext()) {
      if (iterator.next() == null) {
        iterator.remove();
      }
    }

    return collection;
  }

  public static final Map<String, String> stringMap(String... keyValues) {
    Map<String, String> map = new HashMap<>();

    for (int i = 0; i < (keyValues.length / 2); i++) {
      map.put(keyValues[2 * i], keyValues[2 * i + 1]);
    }

    return map;
  }

  /**
   * Metoda dokonujaca redukcji na kolekcji
   *
   * @param list redukowana kolekcja
   * @param reducer reduktor
   * @param <F> typ elementu w kolekcji
   * @param <T> typ wartosci zwracanej
   * @return
   */
  public static <F, T> T fold(
      Collection<? extends F> list, final T startValue, BiFunction<T, F, T> reducer) {
    T value = startValue;

    for (F item : list) {
      value = reducer.apply(value, item);
    }
    return value;
  }

  public static List<Integer> range(final Integer start, final Integer end) {
    List<Integer> range = new ArrayList<>();

    for (int i = start; i < end; i++) {
      range.add(i);
    }

    return range;
  }
}
