package pl.matsuo.core.web.scope;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.AbstractRequestAttributesScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Session scope modification that provides bean even outside of http request processing.
 *
 * <p>Created by marek on 19.07.14.
 */
public class WideSessionScope extends AbstractRequestAttributesScope implements Scope {
  private static final Logger logger = LoggerFactory.getLogger(WideSessionScope.class);

  protected Map<String, ThreadLocal<Object>> objectHolders = new HashMap<>();

  @Override
  protected int getScope() {
    // constant higher than values from RequestAttributes.
    return 13;
  }

  @Override
  public String getConversationId() {
    try {
      return RequestContextHolder.currentRequestAttributes().getSessionId();
    } catch (IllegalStateException e) {
      logger.debug("outside web session");
      return "non_web_";
    }
  }

  @Override
  public Object get(String name, ObjectFactory<?> objectFactory) {
    try {
      Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
      synchronized (mutex) {
        return super.get(name, objectFactory);
      }
    } catch (IllegalStateException e) {
      logger.debug("outside web session");
      ThreadLocal<Object> objectHolder = objectHolders.get(name);
      if (objectHolder == null) {
        objectHolders.put(name, new NamedThreadLocal<>("wideScopeObjectHolder_" + name));
        objectHolder = objectHolders.get(name);
      }

      if (objectHolder.get() == null) {
        objectHolder.set(objectFactory.getObject());
      }
      return objectHolder.get();
    }
  }

  @Override
  public Object remove(String name) {
    try {
      Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
      synchronized (mutex) {
        return super.remove(name);
      }
    } catch (IllegalStateException e) {
      logger.debug("outside web session");
      Object object = objectHolders.get(name).get();
      objectHolders.get(name).set(null);
      return object;
    }
  }

  @Override
  public void registerDestructionCallback(String name, Runnable callback) {
    try {
      logger.debug("outside web session");
      RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
      attributes.registerDestructionCallback(name, callback, getScope());
    } catch (IllegalStateException e) {
      logger.debug("outside web session");
    }
  }
}
