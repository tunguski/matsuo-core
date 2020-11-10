package pl.matsuo.core.util.collection;

import lombok.Value;

@Value
public class Pair<E, F> {

  E key;
  F value;

  public static <E, F> Pair<E, F> pair(E key, F value) {
    return new Pair(key, value);
  }
}
