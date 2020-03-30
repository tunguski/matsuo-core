package pl.matsuo.core.util.function;

import java.util.function.Supplier;

public interface Try<E> {
  E get();

  Try<E> ifFailure(Supplier<Try<E>> supplier);

  default boolean isSuccess() {
    return false;
  }

  default boolean isFailure() {
    return false;
  }
}
