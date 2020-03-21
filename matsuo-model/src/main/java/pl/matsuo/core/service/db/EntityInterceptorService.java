package pl.matsuo.core.service.db;

import static java.util.stream.Collectors.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.interceptor.InterceptorComponent;

/** Created by marek on 25.03.14. */
@Service
public class EntityInterceptorService extends EmptyInterceptor {

  protected List<Interceptor> interceptors;

  @Override
  public boolean onLoad(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    boolean modified = false;
    for (Interceptor interceptor : interceptors) {
      modified = interceptor.onLoad(entity, id, state, propertyNames, types) || modified;
    }
    return modified;
  }

  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types)
      throws CallbackException {
    boolean modified = false;
    for (Interceptor interceptor : interceptors) {
      modified =
          interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types)
              || modified;
    }
    return modified;
  }

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    boolean modified = false;
    for (Interceptor interceptor : interceptors) {
      modified = interceptor.onSave(entity, id, state, propertyNames, types) || modified;
    }
    return modified;
  }

  @Override
  public void onDelete(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.onDelete(entity, id, state, propertyNames, types);
    }
  }

  @Override
  public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.onCollectionRecreate(collection, key);
    }
  }

  @Override
  public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.onCollectionRemove(collection, key);
    }
  }

  @Override
  public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.onCollectionUpdate(collection, key);
    }
  }

  @Override
  public void preFlush(Iterator entities) throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.preFlush(entities);
    }
  }

  @Override
  public void postFlush(Iterator entities) throws CallbackException {
    for (Interceptor interceptor : interceptors) {
      interceptor.postFlush(entities);
    }
  }

  @Override
  public void afterTransactionBegin(Transaction tx) {
    for (Interceptor interceptor : interceptors) {
      interceptor.afterTransactionBegin(tx);
    }
  }

  @Override
  public void beforeTransactionCompletion(Transaction tx) {
    for (Interceptor interceptor : interceptors) {
      interceptor.beforeTransactionCompletion(tx);
    }
  }

  @Override
  public void afterTransactionCompletion(Transaction tx) {
    for (Interceptor interceptor : interceptors) {
      interceptor.afterTransactionCompletion(tx);
    }
  }

  @Autowired
  @InterceptorComponent
  void setInterceptors(List<Interceptor> interceptors) {
    this.interceptors =
        interceptors.stream()
            .filter(
                interceptor ->
                    !EntityInterceptorService.class.isAssignableFrom(interceptor.getClass()))
            .collect(toList());
  }
}
