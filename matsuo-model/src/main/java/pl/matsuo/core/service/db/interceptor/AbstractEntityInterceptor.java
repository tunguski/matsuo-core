package pl.matsuo.core.service.db.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.interceptor.InterceptorComponent;
import pl.matsuo.core.service.session.SessionState;

import java.io.Serializable;
import java.util.function.Supplier;

import static java.util.Arrays.*;


public abstract class AbstractEntityInterceptor extends EmptyInterceptor {
  private static final long serialVersionUID = 1L;


  @Autowired
  protected SessionState sessionState;


  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
                      Object[] previousState, String[] propertyNames, Type[] types) {
    return false;
  }


  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    return false;
  }


  protected void setValue(Object[] currentState, String[] propertyNames, String propertyToSet,
                          Supplier<Object> valueProvider) {
    int index = asList(propertyNames).indexOf(propertyToSet);

    if (index >= 0 && currentState[index] == null) {
      currentState[index] = valueProvider.get();
    }
  }


  protected <E> E getValue(Object[] currentState, String[] propertyNames, String propertyToGet) {
    int index = asList(propertyNames).indexOf(propertyToGet);

    if (index >= 0) {
      return (E) currentState[index];
    } else {
      throw new IllegalArgumentException("No property " + propertyToGet);
    }
  }
}

