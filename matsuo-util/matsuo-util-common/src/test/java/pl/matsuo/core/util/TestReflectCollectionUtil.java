package pl.matsuo.core.util;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.ReflectCollectionUtil.collect;
import static pl.matsuo.core.util.ReflectCollectionUtil.toMap;
import static pl.matsuo.core.util.collection.CollectionUtil.reMap;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

public class TestReflectCollectionUtil {

  @Test
  public void testCollect() {
    assertEquals(asList(7, 3), collect(asList(new X(7), new X(3)), "value"));
  }

  @Test
  public void testToMap() {
    Map<Integer, X> map = toMap(asList(new X(7), new X(3)), "value");

    assertEquals(2, map.size());
    assertTrue(map.keySet().contains(7));
    assertTrue(map.keySet().contains(3));
  }

  @Test
  public void testReMap() {
    Map<Integer, X> map = toMap(asList(new X(7), new X(3)), "value");
    Map<String, X> map2 = reMap(map, key -> key.toString());

    assertEquals(2, map2.size());
    assertTrue(map2.keySet().contains("7"));
    assertTrue(map2.keySet().contains("3"));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  class X {
    private Integer value;
  }
}
