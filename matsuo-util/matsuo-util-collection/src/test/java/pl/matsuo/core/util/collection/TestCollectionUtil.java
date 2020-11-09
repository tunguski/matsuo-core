package pl.matsuo.core.util.collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static pl.matsuo.core.util.collection.CollectionUtil.flatten;
import static pl.matsuo.core.util.collection.CollectionUtil.fold;
import static pl.matsuo.core.util.collection.CollectionUtil.last;
import static pl.matsuo.core.util.collection.CollectionUtil.merge;
import static pl.matsuo.core.util.collection.CollectionUtil.range;
import static pl.matsuo.core.util.collection.CollectionUtil.removeNulls;
import static pl.matsuo.core.util.collection.CollectionUtil.stringMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

public class TestCollectionUtil {

  @Test
  public void testFlatten() {
    assertEquals(asList(1, 2, 3, 4), flatten(asList(1, 2), asList(3, 4)));
  }

  @Test
  public void testLast() {
    assertEquals((Integer) 3, last(asList(1, 2, 3)));
  }

  @Test
  public void testMerge() {
    assertEquals(asList(1, 2, 3, 4), merge(asList(1, 2), asList(3, 4)));
  }

  @Test
  public void testRemoveNulls() {
    assertEquals(asList(1, 3), removeNulls(new ArrayList<Integer>(asList(1, null, 3))));
  }

  @Test
  public void testStringMap() {
    Map<String, String> map = stringMap("1", "one", "2", "two");

    assertEquals(2, map.size());
    assertEquals("one", map.get("1"));
    assertEquals("two", map.get("2"));
  }

  @Test
  public void testFold() {
    assertEquals((Integer) 24, fold(asList(1, 2, 3, 4), 1, (val, next) -> val * next));
  }

  @Test
  public void testRange() {
    List<Integer> list = range(0, 28);
    assertEquals(28, list.size());
    assertEquals((Integer) 0, list.get(0));
    assertEquals((Integer) 27, list.get(27));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  class X {
    private Integer value;
  }
}
