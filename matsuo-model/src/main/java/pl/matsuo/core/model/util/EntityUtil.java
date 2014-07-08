package pl.matsuo.core.model.util;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.service.db.Database;

import java.util.function.Consumer;

/**
 * Created by marek on 29.03.14.
 */
public class EntityUtil {

  public static Consumer<Database> maybeCreate(AbstractEntity entity, Consumer<Database> ... childsToCreate) {
    return database -> {
      if (entity.getId() == null) {
        for (Consumer<Database> createChild : childsToCreate) {
          createChild.accept(database);
        }

        database.create(entity);
      }
    };
  }


  public static <E extends AbstractEntity> Consumer<Database> createOrUpdate(E entity, Consumer<E> onCreate, Consumer<E> onUpdate) {
    return database -> {
      if (entity.getId() != null) {
        onUpdate.accept(entity);
        database.update(entity);
      } else {
        onCreate.accept(entity);
        database.create(entity);
      }
    };
  }


  public static <E extends AbstractEntity> Consumer<Database> createOrUpdate(E entity, Consumer<E> onPersist) {
    return createOrUpdate(entity, onPersist, onPersist);
  }


  public static <E extends AbstractEntity> Consumer<Database> createOrUpdate(E entity) {
    return createOrUpdate(entity, value -> {}, value -> {});
  }
}
