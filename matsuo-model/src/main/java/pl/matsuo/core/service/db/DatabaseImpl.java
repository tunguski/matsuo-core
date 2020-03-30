package pl.matsuo.core.service.db;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_NO;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

  @Autowired protected SessionFactory sessionFactory;
  protected AutowireCapableBeanFactory beanFactory;
  @Autowired protected SessionState sessionState;

  private Session session() {
    return sessionFactory.getCurrentSession();
  }

  //  @PostConstruct
  //  public void startDbGui() {
  //    DatabaseManagerSwing.main(new String[]{ "--url", "jdbc:hsqldb:mem:test", "--user", "sa",
  // "--noexit"});
  //  }

  @Override
  public <E extends AbstractEntity> E findById(
      Class<E> clazz, Integer id, Initializer<? super E>... initializers) {
    E element = (E) session().get(clazz, id);

    Assert.notNull(element);

    initializeEntity(element, initializers);

    return element;
  }

  @Override
  public <E extends AbstractEntity> List<E> findAll(
      Class<E> clazz, Initializer<? super E>... initializers) {
    List<E> list = find(query(clazz));

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
    session().save(element);
    return element;
  }

  @Override
  public <E extends AbstractEntity> E update(E element) {
    session().update(element);
    return element;
  }

  @Override
  public void delete(Class<? extends AbstractEntity> clazz, Integer id) {
    session().delete(findById(clazz, id));
  }

  @Override
  public void delete(AbstractEntity entity) {
    session().delete(entity);
  }

  @Override
  public void evict(Object... objects) {
    for (Object object : objects) {
      session().evict(object);
    }
  }

  @Override
  public <E extends AbstractEntity> List<E> find(Query<E> query) {
    beanFactory.autowireBeanProperties(query, AUTOWIRE_NO, true);
    return query.query(sessionState.getIdBucket());
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
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
  }
}
