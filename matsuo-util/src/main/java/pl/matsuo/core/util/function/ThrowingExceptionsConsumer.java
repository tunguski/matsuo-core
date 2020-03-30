package pl.matsuo.core.util.function;

public interface ThrowingExceptionsConsumer<E> {
  void accept(E value) throws Exception;
}
