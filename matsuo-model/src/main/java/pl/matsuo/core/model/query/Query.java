package pl.matsuo.core.model.query;

import pl.matsuo.core.model.AbstractEntity;

import java.util.List;


public interface Query<E extends AbstractEntity> {


  List<E> query(Integer idBucket);
}

