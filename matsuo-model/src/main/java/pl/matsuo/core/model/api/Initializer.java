package pl.matsuo.core.model.api;

public interface Initializer<E> {

  void init(E element);

  default void maybeInit(E element) {
    if (element != null) {
      init(element);
    }
  }
}
