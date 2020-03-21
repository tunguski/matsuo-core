package pl.matsuo.core.service.db.interceptor;

import java.io.Serializable;
import java.util.function.Supplier;
import org.hibernate.type.Type;
import org.springframework.beans.factory.BeanCreationException;
import pl.matsuo.core.model.interceptor.InterceptorComponent;

@InterceptorComponent
public class IdBucketInterceptor extends AbstractEntityInterceptor {

  Supplier<Object> idBucketSupplier =
      () -> {
        try {
          return sessionState != null ? sessionState.getIdBucket() : null;
        } catch (BeanCreationException e) {
          return null;
        }
      };

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (getValue(state, propertyNames, "idBucket") == null) {
      setValue(state, propertyNames, "idBucket", idBucketSupplier);
      return true;
    }

    return false;
  }
}
