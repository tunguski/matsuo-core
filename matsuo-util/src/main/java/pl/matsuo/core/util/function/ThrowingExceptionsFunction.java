package pl.matsuo.core.util.function;

public interface ThrowingExceptionsFunction<E, F> {
  F apply(E value) throws Exception;
}
