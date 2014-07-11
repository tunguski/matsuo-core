package pl.matsuo.core.service.db.interceptor;

import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Supplier;


public class AuditTrailInterceptor extends AbstractEntityInterceptor {
  private static final long serialVersionUID = 1L;


  Supplier<Object> idUserSupplier = () -> sessionState.getUser().getId();


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
}

