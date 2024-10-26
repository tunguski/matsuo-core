package pl.matsuo.core.service.db;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_NO;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.service.session.SessionState;

@Repository
@Transactional
public class DatabaseImpl implements Database, BeanFactoryAware {

  @PersistenceContext protected EntityManager entityManager;
  protected AutowireCapableBeanFactory beanFactory;
  @Autowired protected SessionState sessionState;

  //  @PostConstruct
  //  public void startDbGui() {
  //    DatabaseManagerSwing.main(new String[]{ "--url", "jdbc:hsqldb:mem:test", "--user", "sa",
  // "--noexit"});
  //  }

  @Override
  public long count(Class<? extends AbstractEntity> clazz, Predicate predicate) {
    return queryDslQueryBase(clazz, predicate).fetchCount();
  }

  @Override
  public boolean exists(Class<? extends AbstractEntity> clazz, Predicate predicate) {
    return queryDslQueryBase(clazz, predicate).fetchFirst() != null;
  }

  @Override
  public <E extends AbstractEntity> E findById(
      Class<E> clazz, Long id, Initializer<? super E>... initializers) {
    E element = (E) entityManager.find(clazz, id);

    Assert.notNull(element, "No entity found");

    initializeEntity(element, initializers);

    return element;
  }

  @Override
  public <E extends AbstractEntity> List<E> findAll(
      Class<E> clazz, Initializer<? super E>... initializers) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
    CriteriaQuery<E> select = query.select(query.from(clazz));
    TypedQuery<E> typedQuery = entityManager.createQuery(select);
    List<E> list = typedQuery.getResultList();

    for (E element : list) {
      initializeEntity(element, initializers);
    }

    return list;
  }

  protected <E extends AbstractEntity> void initializeEntity(
      E element, Initializer<? super E>... initializers) {
    if (sessionState != null
        && sessionState.getIdBucket() != null
        && !sessionState.getIdBucket().equals(element.getIdBucket())) {
      throw new RuntimeException("Unauthorized data access");
    }

    for (Initializer<? super E> initializer : initializers) {
      initializer.init(element);
    }
  }

  @Override
  public <E extends AbstractEntity> E create(E element) {
    entityManager.persist(element);
    return element;
  }

  @Override
  public <E extends AbstractEntity> E update(E element) {
    return entityManager.merge(element);
  }

  @Override
  public void delete(Class<? extends AbstractEntity> clazz, Long id) {
    delete(findById(clazz, id));
  }

  @Override
  public void delete(AbstractEntity entity) {
    entityManager.remove(entity);
  }

  @Override
  public void evict(Object... objects) {
    for (Object object : objects) {
      entityManager.detach(object);
    }
  }

  @Override
  public <E extends AbstractEntity> List<E> find(Query<E> query) {
    beanFactory.autowireBeanProperties(query, AUTOWIRE_NO, true);
    return query.query(sessionState.getIdBucket());
  }

  @Override
  public <E> List<E> customSelect(ISelectDefinition<E> query) {
    return query.apply(new JPAQueryFactory(entityManager)).fetch();
  }

  Map<Class<?>, EntityPathBase<?>> tables = new HashMap<>();

  <E extends AbstractEntity> EntityPathBase<E> getTable(Class<?> clazz) {
    if (tables.containsKey(clazz)) {
      return (EntityPathBase<E>) tables.get(clazz);
    } else {
      synchronized (this) {
        try {
          Class<?> qClass =
              getClass()
                  .getClassLoader()
                  .loadClass(clazz.getPackage().getName() + ".Q" + clazz.getSimpleName());
          EntityPathBase<E> pathBase =
              (EntityPathBase<E>)
                  qClass.getField(StringUtils.uncapitalize(clazz.getSimpleName())).get(null);

          tables.put(clazz, pathBase);
          return pathBase;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  @Override
  public <E extends AbstractEntity> List<E> find(Class<E> clazz, Predicate predicate) {
    return queryDslQueryBase(clazz, predicate).fetch();
  }

  private <E extends AbstractEntity> JPAQuery<E> queryDslQueryBase(
      Class<E> clazz, Predicate predicate) {
    EntityPathBase<E> pathBase = getTable(clazz);
    return new JPAQueryFactory(entityManager).selectFrom(pathBase).where(predicate);
  }

  @Override
  public <E extends AbstractEntity> List<E> findAsAdmin(Query<E> query) {
    beanFactory.autowireBeanProperties(query, AUTOWIRE_NO, true);
    return query.query(null);
  }

  @Override
  public <E extends AbstractEntity> E findOne(Query<E> query) {
    List<E> result = find(query);
    if (result.isEmpty()) {
      return null;
    } else {
      return result.get(0);
    }
  }

  @Override
  public <E extends AbstractEntity> E findOne(Class<E> clazz, Predicate predicate) {
    List<E> result = find(clazz, predicate);

    if (result.isEmpty()) {
      return null;
    } else {
      return result.get(0);
    }
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
  }
}
