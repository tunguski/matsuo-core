package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.query.AbstractQuery;


/**
 * Created by tunguski on 26.09.13.
 */
public class FromPart implements QueryPart {


  private final String joinType;
  private final String alias;
  private final String joinPath;
  protected Condition joinCondition;


  public FromPart(String joinType, String alias, String joinPath) {
    this.joinType = joinType;
    this.alias = alias;
    this.joinPath = joinPath;
  }


  @Override
  public String print(AbstractQuery query) {
    return joinType + " " + joinPath + " " + alias;
  }


  public Condition getJoinCondition() {
    return joinCondition;
  }
}
