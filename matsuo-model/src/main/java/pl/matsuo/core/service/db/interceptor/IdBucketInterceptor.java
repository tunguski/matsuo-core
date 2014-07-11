package pl.matsuo.core.service.db.interceptor;

import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.function.Supplier;


public class IdBucketInterceptor extends AbstractEntityInterceptor {
  private static final long serialVersionUID = 1L;


  Supplier<Object> idBucketSupplier = () -> sessionState.getUser().getIdBucket();


  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (getValue(state, propertyNames, "idBucket") == null) {
      setValue(state, propertyNames, "idBucket", idBucketSupplier);
      return true;
    }

    return false;
  }
}

