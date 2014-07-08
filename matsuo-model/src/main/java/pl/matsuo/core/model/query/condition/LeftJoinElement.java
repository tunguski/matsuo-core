package pl.matsuo.core.model.query.condition;

/**
 * Created by tunguski on 26.09.13.
 */
public class LeftJoinElement extends FromPart {


  public LeftJoinElement(String alias, Class clazz, Condition joinCondition) {
    super(",", alias, clazz.getName());
    this.joinCondition = joinCondition;
  }


  public LeftJoinElement(String alias, String clazz, Condition joinCondition) {
    super(",", alias, clazz);
    this.joinCondition = joinCondition;
  }
}
