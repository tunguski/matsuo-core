package pl.matsuo.core.util.function;

public interface ThrowingExceptionsSupplier<E> {
  E get() throws Exception;
}
