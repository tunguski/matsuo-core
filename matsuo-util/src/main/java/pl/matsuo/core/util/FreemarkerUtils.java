package pl.matsuo.core.util;

import java.util.Map;


/**
 * Helper util for maps in freemarker. By wrapping map with mapWrapper() you can get map elements by simply
 * lookup(id). Normal maps are changed internally by freemarker and their behaviour is weird.
 */
public class FreemarkerUtils {


  public static class FreemarkerMapWrapper {
    private final Map<? extends Number, ?> map;
    public FreemarkerMapWrapper(Map<? extends Number, ?> map) {
      this.map = map;
    }
    public Object lookup(Number key) {
      if (map.get(key) == null) {
        return null;
      }

      return map.get(key);
    }
  }


  public static Object mapWrapper(final Map<? extends Number, ?> map) {
    return new FreemarkerMapWrapper(map);
  }
}
