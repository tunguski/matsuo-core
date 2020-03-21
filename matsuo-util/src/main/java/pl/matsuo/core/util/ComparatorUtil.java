package pl.matsuo.core.util;

import java.util.Comparator;
import java.util.function.Function;

/** Created by marek on 11.02.15. */
public class ComparatorUtil {

  public static <T> Comparator<T> comparator(Function<T, Comparable>... getters) {

    return (o1, o2) -> {
      for (Function<T, Comparable> getter : getters) {
        Comparable o1Result = getter.apply(o1);
        Comparable o2Result = getter.apply(o2);

        if (o1Result == null && o2Result == null) {
          // do nothin'
        } else if (o1Result == null || o2Result == null) {
          return o1Result == null ? -1 : 1;
        } else {
          int compareResult = o1Result.compareTo(o2Result);
          if (compareResult != 0) {
            return compareResult;
          }
        }
      }

      return 0;
    };
  }
}
