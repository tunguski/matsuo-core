package pl.matsuo.core.model.query.condition;

import java.util.function.Function;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.query.AbstractQuery;

/** Nadklasa dla funkcji jednoargumentowych. */
public class AbstractQueryFunction<T extends AbstractEntity, R> implements QueryFunction {

  private Function<T, R> getter;
  private String functionName;

  public AbstractQueryFunction(Function<T, R> getter, String functionName) {
    this.getter = getter;
    this.functionName = functionName;
  }

  @Override
  public String print(AbstractQuery query) {
    return functionName + "(" + query.resolveFieldName(getter) + ")";
  }
}
