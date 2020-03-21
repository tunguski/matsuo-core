package pl.matsuo.core.util.function;

/** Created by marek on 01.04.14. */
public interface ThrowingExceptionsFunction<E, F> {
  F apply(E value) throws Exception;
}
