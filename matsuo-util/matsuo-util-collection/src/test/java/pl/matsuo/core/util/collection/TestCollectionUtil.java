package pl.matsuo.core.util.collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.collection.CollectionUtil.allMatch;
import static pl.matsuo.core.util.collection.CollectionUtil.anyMatch;
import static pl.matsuo.core.util.collection.CollectionUtil.filterMap;
import static pl.matsuo.core.util.collection.CollectionUtil.fold;
import static pl.matsuo.core.util.collection.CollectionUtil.last;
import static pl.matsuo.core.util.collection.CollectionUtil.merge;
import static pl.matsuo.core.util.collection.CollectionUtil.noneMatch;
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
    assertEquals(asList(1, 2, 3, 4), merge(asList(asList(1, 2), asList(3, 4))));
  }

  @Test
  public void testLast() {
    assertEquals((Integer) 3, last(asList(1, 2, 3)));
  }

  @Test
  public void testMerge() {
    assertEquals(asList(1, 2, 3, 4), merge(asList(asList(1, 2), asList(3, 4))));
  }

  @Test
  public void testFilterMap() {
    assertEquals(asList(1, 4), filterMap(asList(-2, -1, 0, 1, 2), n -> n > 0 ? n * n : null));
  }

  @Test
  public void testFilterMap2() {
    assertEquals(asList(1, 4), filterMap(asList(-2, -1, 0, 1, 2), n -> n > 0, n -> n * n));
  }

  @Test
  public void testRemoveNulls() {
    assertEquals(asList(1, 3), removeNulls(new ArrayList<>(asList(1, null, 3))));
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

  @Test
  public void testAnyMatch() {
    assertTrue(anyMatch(asList(-2, -1, 0, 1, 2), n -> n > 0));
    assertFalse(anyMatch(asList(-2, -1, 0, 1, 2), n -> n > 2));
  }

  @Test
  public void testNoneMatch() {
    assertTrue(noneMatch(asList(-2, -1, 0, 1, 2), n -> n > 2));
    assertFalse(noneMatch(asList(-2, -1, 0, 1, 2), n -> n > 0));
  }

  @Test
  public void testAllMatch() {
    assertTrue(allMatch(asList(-2, -1, 0, 1, 2), n -> n < 10));
    assertFalse(allMatch(asList(-2, -1, 0, 1, 2), n -> n > 0));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  class X {
    private Integer value;
  }
}
