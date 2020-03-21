package pl.matsuo.core.model.query.condition;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.query.AbstractQuery;

public interface QueryPart<E extends AbstractEntity> {
  String print(AbstractQuery<E> query);
}
