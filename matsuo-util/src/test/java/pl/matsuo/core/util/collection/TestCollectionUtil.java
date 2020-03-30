package pl.matsuo.core.util.collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.collection.CollectionUtil.collect;
import static pl.matsuo.core.util.collection.CollectionUtil.flatten;
import static pl.matsuo.core.util.collection.CollectionUtil.fold;
import static pl.matsuo.core.util.collection.CollectionUtil.last;
import static pl.matsuo.core.util.collection.CollectionUtil.merge;
import static pl.matsuo.core.util.collection.CollectionUtil.range;
import static pl.matsuo.core.util.collection.CollectionUtil.reMap;
import static pl.matsuo.core.util.collection.CollectionUtil.removeNulls;
import static pl.matsuo.core.util.collection.CollectionUtil.stringMap;
import static pl.matsuo.core.util.collection.CollectionUtil.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TestCollectionUtil {

  @Test
  public void testCollect() throws Exception {
    assertEquals(asList(7, 3), collect(asList(new X(7), new X(3)), "value"));
  }

  @Test
  public void testFlatten() throws Exception {
    assertEquals(asList(1, 2, 3, 4), flatten(asList(1, 2), asList(3, 4)));
  }

  @Test
  public void testToMap() throws Exception {
    Map<Integer, X> map = toMap(asList(new X(7), new X(3)), "value");

    assertEquals(2, map.size());
    assertTrue(map.keySet().contains(7));
    assertTrue(map.keySet().contains(3));
  }

  @Test
  public void testReMap() throws Exception {
    Map<Integer, X> map = toMap(asList(new X(7), new X(3)), "value");
    Map<String, X> map2 = reMap(map, key -> key.toString());

    assertEquals(2, map2.size());
    assertTrue(map2.keySet().contains("7"));
    assertTrue(map2.keySet().contains("3"));
  }

  @Test
  public void testLast() throws Exception {
    assertEquals((Integer) 3, last(asList(1, 2, 3)));
  }

  @Test
  public void testMerge() throws Exception {
    assertEquals(asList(1, 2, 3, 4), merge(asList(1, 2), asList(3, 4)));
  }

  @Test
  public void testRemoveNulls() throws Exception {
    assertEquals(asList(1, 3), removeNulls(new ArrayList<Integer>(asList(1, null, 3))));
  }

  @Test
  public void testStringMap() throws Exception {
    Map<String, String> map = stringMap("1", "one", "2", "two");

    assertEquals(2, map.size());
    assertEquals("one", map.get("1"));
    assertEquals("two", map.get("2"));
  }

  @Test
  public void testFold() throws Exception {
    assertEquals((Integer) 24, fold(asList(1, 2, 3, 4), 1, (val, next) -> val * next));
  }

  @Test
  public void testRange() throws Exception {
    List<Integer> list = range(0, 28);
    assertEquals(28, list.size());
    assertEquals((Integer) 0, list.get(0));
    assertEquals((Integer) 27, list.get(27));
  }

  class X {

    private Integer value;

    public X(Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return value;
    }

    public void setValue(Integer value) {
      this.value = value;
    }
  }
}
