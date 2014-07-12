package pl.matsuo.core.service.db;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.Initializer;
import pl.matsuo.core.model.query.Query;

import java.util.List;

public interface Database {


  <E extends AbstractEntity> E findById(Class<E> clazz, Integer id, Initializer<? super E>... initializers);


  <E extends AbstractEntity> List<E> findAll(Class<E> clazz, Initializer<? super E> ... initializers);


  <E extends AbstractEntity> List<E> find(Query<E> query);
  <E extends AbstractEntity> E findOne(Query<E> query);


  /**
   * Special search case when user don't want to check buckets and get all data. Potentialy dangerous, but needed for
   * some admin operations.
   */
  <E extends AbstractEntity> List<E> findAsAdmin(Query<E> query);


  <E extends AbstractEntity> E create(E element);
  <E extends AbstractEntity> E update(E element);


  void delete(Class<? extends AbstractEntity> clazz, Integer id);
  void delete(AbstractEntity abstractEntity);

  void evict(Object ... objects);
}

