package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.query.AbstractQuery;


/**
 * Nadklasa dla funkcji jednoargumentowych.
 */
public class AbstractQueryFunction implements QueryFunction {


  private String fieldName;
  private String functionName;


  public AbstractQueryFunction(String fieldName, String functionName) {
    this.fieldName = fieldName;
    this.functionName = functionName;
  }


  @Override
  public String print(AbstractQuery query) {
    return functionName + "(" + fieldName + ")";
  }
}
