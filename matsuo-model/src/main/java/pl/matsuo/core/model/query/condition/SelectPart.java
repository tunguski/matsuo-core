package pl.matsuo.core.model.query.condition;

import com.google.common.base.Joiner;
import pl.matsuo.core.model.query.AbstractQuery;


/**
 * Created by tunguski on 26.09.13.
 */
public class SelectPart implements QueryPart {


  private String[] fields;


  public SelectPart(String ... fields) {
    this.fields = fields;
  }


  @Override
  public String print(AbstractQuery query) {
    return Joiner.on(", ").join(fields);
  }
}
