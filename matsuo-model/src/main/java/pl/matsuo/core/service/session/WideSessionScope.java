package pl.matsuo.core.service.session;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Session scope modification that provides bean even outside of http request processing.
 *
 * Created by marek on 19.07.14.
 */
public class WideSessionScope implements Scope {
  private Map<String, Object> objectMap = Collections
      .synchronizedMap(new HashMap<String, Object>());

  public Object get(String name, ObjectFactory<?> objectFactory) {
    if (!objectMap.containsKey(name)) {
      objectMap.put(name, objectFactory.getObject());
    }
    return objectMap.get(name);

  }

  public Object remove(String name) {
    return objectMap.remove(name);
  }

  public void registerDestructionCallback(String name, Runnable callback) {
    // do nothing
  }

  public Object resolveContextualObject(String key) {
    return null;
  }

  public String getConversationId() {
    return "MyScope";
  }

  /**
   * clear the beans
   */
  public void clearBean() {
    objectMap.clear();
  }
}

