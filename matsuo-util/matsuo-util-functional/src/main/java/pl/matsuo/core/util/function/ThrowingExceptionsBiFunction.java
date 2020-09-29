package pl.matsuo.core.util.function;

public interface ThrowingExceptionsBiFunction<D, E, F> {
  F apply(D first, E second) throws Exception;
}
