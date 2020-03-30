package pl.matsuo.core.util.function;

import java.util.function.Supplier;

public class Success<E> implements Try<E> {

  private E value;

  public Success(E value) {
    this.value = value;
  }

  @Override
  public E get() {
    return value;
  }

  @Override
  public Try<E> ifFailure(Supplier<Try<E>> supplier) {
    return this;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }
}
