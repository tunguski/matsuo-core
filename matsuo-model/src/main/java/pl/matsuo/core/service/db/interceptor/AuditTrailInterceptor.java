package pl.matsuo.core.service.db.interceptor;

import java.io.Serializable;
import java.time.Instant;
import java.util.function.Supplier;
import org.hibernate.type.Type;
import pl.matsuo.core.model.interceptor.InterceptorComponent;

@InterceptorComponent
public class AuditTrailInterceptor extends AbstractEntityInterceptor {

  Supplier<Object> idUserSupplier =
      () ->
          sessionState != null && sessionState.getUser() != null
              ? sessionState.getUser().getId()
              : null;

  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    setValue(currentState, propertyNames, "idUserUpdated", idUserSupplier);
    setValue(currentState, propertyNames, "updatedTime", Instant::now);
    return true;
  }

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    setValue(state, propertyNames, "idUserCreated", idUserSupplier);
    setValue(state, propertyNames, "createdTime", Instant::now);
    return true;
  }
}
