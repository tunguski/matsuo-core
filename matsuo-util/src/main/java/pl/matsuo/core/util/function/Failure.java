package pl.matsuo.core.util.function;

import java.util.function.Supplier;

public class Failure<E> implements Try<E> {

  @Override
  public E get() {
    throw new RuntimeException("Cannot get value from failure");
  }

  @Override
  public Try<E> ifFailure(Supplier<Try<E>> supplier) {
    return supplier.get();
  }

  @Override
  public boolean isFailure() {
    return true;
  }
}
