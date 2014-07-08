package pl.matsuo.core.service.db.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.interceptor.InterceptorComponent;
import pl.matsuo.core.service.session.SessionState;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Supplier;

import static java.util.Arrays.*;


@InterceptorComponent
public class AuditTrailInterceptor extends EmptyInterceptor {
  private static final long serialVersionUID = 1L;


  @Autowired
  protected SessionState sessionState;


  Supplier<Object> idUserSupplier = () -> { return sessionState.getUser().getId(); };


  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
                      Object[] previousState, String[] propertyNames, Type[] types) {
    setValue(currentState, propertyNames, "idUserUpdated", idUserSupplier);
    setValue(currentState, propertyNames, "updatedTime", () -> new Date());
    return true;
  }


  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    setValue(state, propertyNames, "idUserCreated", idUserSupplier);
    setValue(state, propertyNames, "createdTime", () -> new Date());
    return true;
  }


  private void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Supplier<Object> valueProvider) {
    int index = asList(propertyNames).indexOf(propertyToSet);

    if (index >= 0 && currentState[index] == null) {
      currentState[index] = valueProvider.get();
    }
  }
}

