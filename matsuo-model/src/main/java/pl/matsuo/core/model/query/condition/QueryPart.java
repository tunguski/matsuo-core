package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.query.AbstractQuery;

/** Created by tunguski on 26.09.13. */
public interface QueryPart<E extends AbstractEntity> {
  String print(AbstractQuery<E> query);
}
