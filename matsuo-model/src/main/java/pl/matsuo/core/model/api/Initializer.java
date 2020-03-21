package pl.matsuo.core.model.api;

/** Created by tunguski on 19.09.13. */
public interface Initializer<E> {

  void init(E element);

  default void maybeInit(E element) {
    if (element != null) {
      init(element);
    }
  }
}
