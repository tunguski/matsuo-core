package pl.matsuo.core.util;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pl.matsuo.core.util.ComparatorUtil.comparator;
import static pl.matsuo.core.util.DateUtil.date;

import java.util.Date;
import java.util.List;
import org.junit.Test;

public class TestComparatorUtil {

  private class X {

    final Integer integer;
    final String string;
    final Date date;

    private X(Integer integer, String string, Date date) {
      this.integer = integer;
      this.string = string;
      this.date = date;
    }

    public Integer getInteger() {
      return integer;
    }

    public String getString() {
      return string;
    }

    public Date getDate() {
      return date;
    }
  }

  public List<X> list() {
    return asList(
        new X(null, null, null),
        new X(3, "c", date(2015, 0, 3)),
        new X(2, "b", date(2015, 0, 2)),
        new X(1, "a", date(2015, 0, 1)),
        new X(null, null, null));
  }

  @Test
  public void testComparator() {
    asList(comparator(X::getInteger), comparator(X::getDate), comparator(X::getString))
        .forEach(
            comparator -> {
              List<X> list = list();
              list.sort(comparator);

              assertNull(list.get(0).getInteger());
              assertNull(list.get(1).getInteger());
              assertEquals((Integer) 1, list.get(2).getInteger());
              assertEquals((Integer) 2, list.get(3).getInteger());
              assertEquals((Integer) 3, list.get(4).getInteger());
            });
  }
}
