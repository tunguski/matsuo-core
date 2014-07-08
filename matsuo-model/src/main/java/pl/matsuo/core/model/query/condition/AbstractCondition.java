package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.query.AbstractQuery;


/**
 * Nadklasa dla prostych warunków opartych na dwuargumentowym operatorze.
 */
public class AbstractCondition implements Condition {


  private String fieldName;
  private String operator;
  private Object value;


  public AbstractCondition(String fieldName, String operator, Object value) {
    this.fieldName = fieldName;
    this.operator = operator;
    this.value = value;
  }


  @Override
  public String print(AbstractQuery query) {
    if (value == null) {
      return "1 = 1";
    }

    return fieldName + " " + operator + " " + query.propertyValue(value);
  }
}
