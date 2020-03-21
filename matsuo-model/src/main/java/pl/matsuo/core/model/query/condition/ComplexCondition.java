package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.query.AbstractQuery;

/** Created by tunguski on 19.09.13. */
public class ComplexCondition implements Condition {

  private String joiner;
  private final Condition[] conditions;

  public ComplexCondition(String joiner, Condition... conditions) {
    this.joiner = joiner;
    this.conditions = conditions;

    if (conditions.length == 0) {
      throw new IllegalStateException(
          "ComplexCondition must containt one or more internal conditions");
    }
  }

  @Override
  public String print(AbstractQuery query) {
    StringBuilder sb = new StringBuilder("(");

    for (Condition condition : conditions) {
      sb.append(condition.print(query) + " " + joiner + " ");
    }

    // usuwa ostatni joiner
    sb.delete(sb.length() - joiner.length() - 2, sb.length());

    return sb.toString().trim() + ")";
  }
}
