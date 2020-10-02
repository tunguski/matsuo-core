package pl.matsuo.core.service.db;

import com.querydsl.core.types.Predicate;
import java.util.List;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.Query;

public interface Database {

  long count(Class<? extends AbstractEntity> clazz, Predicate predicate);

  boolean exists(Class<? extends AbstractEntity> clazz, Predicate predicate);

  <E extends AbstractEntity> E findById(
      Class<E> clazz, Long id, Initializer<? super E>... initializers);

  <E extends AbstractEntity> List<E> findAll(
      Class<E> clazz, Initializer<? super E>... initializers);

  <E extends AbstractEntity> List<E> find(Query<E> query);

  <E> List<E> customSelect(ISelectDefinition<E> query);

  <E extends AbstractEntity> List<E> find(Class<E> clazz, Predicate predicate);

  <E extends AbstractEntity> E findOne(Query<E> query);

  <E extends AbstractEntity> E findOne(Class<E> clazz, Predicate predicate);

  /**
   * Special search case when user don't want to check buckets and get all data. Potentially
   * dangerous, but needed for some admin operations.
   */
  <E extends AbstractEntity> List<E> findAsAdmin(Query<E> query);

  <E extends AbstractEntity> E create(E element);

  <E extends AbstractEntity> E update(E element);

  void delete(Class<? extends AbstractEntity> clazz, Long id);

  void delete(AbstractEntity abstractEntity);

  void evict(Object... objects);
}
