package pl.matsuo.core.service.db;

import java.util.List;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.Query;

public interface DatabaseMethods {

  Database getDatabase();

  default <E extends AbstractEntity> E findById(
      Class<E> clazz, Integer id, Initializer<E>... initializers) {
    return getDatabase().findById(clazz, id, initializers);
  };

  default <E extends AbstractEntity> List<E> findAll(
      Class<E> clazz, Initializer<E>... initializers) {
    return getDatabase().findAll(clazz, initializers);
  };

  default <E extends AbstractEntity> List<E> find(Query<E> query) {
    return getDatabase().find(query);
  };

  default <E extends AbstractEntity> E findOne(Query<E> query) {
    return getDatabase().findOne(query);
  };
}
