package pl.matsuo.core.model.query;

import java.util.List;
import pl.matsuo.core.model.AbstractEntity;

public interface Query<E extends AbstractEntity> {

  List<E> query(Long idBucket);
}
