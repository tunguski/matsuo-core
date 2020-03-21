package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.AbstractEntity;

/** Created by tunguski on 26.09.13. */
public class LeftJoinElement<E extends AbstractEntity, J extends AbstractEntity>
    extends FromPart<E> {

  public LeftJoinElement(String alias, Class<J> clazz, Condition joinCondition) {
    super(",", alias, clazz.getName());
    this.joinCondition = joinCondition;
  }

  public LeftJoinElement(String alias, String clazz, Condition joinCondition) {
    super(",", alias, clazz);
    this.joinCondition = joinCondition;
  }
}
