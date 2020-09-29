package pl.matsuo.core.util;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.DateUtil.addTime;
import static pl.matsuo.core.util.DateUtil.between;
import static pl.matsuo.core.util.DateUtil.cal;
import static pl.matsuo.core.util.DateUtil.date;
import static pl.matsuo.core.util.DateUtil.dateAndTime;
import static pl.matsuo.core.util.DateUtil.getQuaterEnd;
import static pl.matsuo.core.util.DateUtil.getQuaterStart;
import static pl.matsuo.core.util.DateUtil.isoFormat;
import static pl.matsuo.core.util.DateUtil.max;
import static pl.matsuo.core.util.DateUtil.maybeDate;
import static pl.matsuo.core.util.DateUtil.min;
import static pl.matsuo.core.util.DateUtil.time;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

public class TestDateUtil {

  @Test
  public void testDate() {
    assertEquals(1398170096789L, date("2014-04-22T12:34:56.789Z").getTime());
    assertEquals(1398170096000L, date("2014-04-22T12:34:56Z").getTime());
    assertEquals(1398170096789L, date("\"2014-04-22T12:34:56.789Z\"").getTime());
    assertEquals(1398170096789L, date("1398170096789").getTime());
  }

  @Test
  public void testIsoFormat() {
    Calendar cal = getInstance();
    cal.setTimeInMillis(1398170096789L);
    assertEquals("2014-04-22T12:34:56Z", isoFormat(cal.getTime()));
  }

  @Test
  public void testDate1() {
    Date date = date(new Date(100, 1, 2), 10, 20);
    assertEquals(100, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(10, date.getHours());
    assertEquals(20, date.getMinutes());
  }

  @Test
  public void testDate2() {
    Date date = date(2020, 1, 2);
    assertEquals(120, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(0, date.getHours());
    assertEquals(0, date.getMinutes());
  }

  @Test
  public void testMaybeDate() {
    assertNull(maybeDate("test"));
    assertNotNull(maybeDate("2020-01-02T15:19:21+00:00"));
  }

  @Test
  public void testDate3() {
    Date date = date(2020, 1, 2, 8, 25);
    assertEquals(120, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(8, date.getHours());
    assertEquals(25, date.getMinutes());
  }

  @Test
  public void testDate4() {
    Date date = date("2020-01-02T15:19:21+00:00");
    assertEquals(120, date.getYear());
    assertEquals(0, date.getMonth());
    assertEquals(2, date.getDate());
    // assertEquals(16, date.getHours());
    assertEquals(19, date.getMinutes());
  }

  @Test
  public void testAddTime() {
    Date date = addTime(date(2000, 7, 10), MINUTE, 17);
    assertEquals(0, date.getHours());
    assertEquals(17, date.getMinutes());
  }

  @Test
  public void testCal() {
    Calendar cal = cal(2015, 4, 22, 10, 20);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }

  @Test
  public void testCal1() {
    Calendar cal = cal(2015, 4, 22);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(0, cal.get(HOUR));
    assertEquals(0, cal.get(MINUTE));
  }

  @Test
  public void testCal2() {
    Calendar cal = cal(date(2015, 4, 22, 10, 20));
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }

  @Test
  public void testCal3() {
    Calendar cal = cal(date(2015, 4, 22, 10, 20).getTime());
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }

  @Test
  public void testCal4() {
    Calendar cal = cal(date(2015, 4, 22), 10, 20);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }

  @Test
  public void testTime() {
    Time time = time(10, 20);
    assertEquals(10, time.getHours());
    assertEquals(20, time.getMinutes());
  }

  @Test
  public void testDateAndTime() {
    Date date = dateAndTime(date(2015, 4, 22), time(10, 20));
    assertEquals(115, date.getYear());
    assertEquals(4, date.getMonth());
    assertEquals(22, date.getDate());
    assertEquals(10, date.getHours());
    assertEquals(20, date.getMinutes());
  }

  @Test
  public void testMin() {
    assertEquals(date(2014, 7, 12), min(date(2015, 1, 7), date(2014, 7, 12)));
  }

  @Test
  public void testMax() {
    assertEquals(date(2015, 1, 7), max(date(2015, 1, 7), date(2014, 7, 12)));
  }

  @Test
  public void testMin1() {
    assertEquals(cal(2014, 7, 12), min(cal(2015, 1, 7), cal(2014, 7, 12)));
  }

  @Test
  public void testMax1() {
    assertEquals(cal(2015, 1, 7), max(cal(2015, 1, 7), cal(2014, 7, 12)));
  }

  @Test
  public void testBetween() {
    assertTrue(between(date(2014, 9, 12), date(2014, 7, 12), date(2015, 1, 7)));
    assertFalse(between(date(2012, 9, 12), date(2014, 7, 12), date(2015, 1, 7)));
  }

  @Test
  public void testSqlDate() {}

  @Test
  public void testSqlDate1() {}

  @Test
  public void testSqlDate2() {}

  @Test
  public void testSqlDate3() {}

  @Test
  public void testIsoFormat1() {}

  @Test
  public void testLocalDate() {}

  @Test
  public void testGetQuaterStart() {
    assertEquals(date(2014, 0, 1), getQuaterStart(date(2014, 0, 15)));
    assertEquals(date(2014, 0, 1), getQuaterStart(date(2014, 2, 30)));
    assertEquals(date(2014, 3, 1), getQuaterStart(date(2014, 4, 15)));
    assertEquals(date(2014, 6, 1), getQuaterStart(date(2014, 8, 30)));
    assertEquals(date(2014, 9, 1), getQuaterStart(date(2014, 11, 31)));
  }

  @Test
  public void testGetQuaterEnd() {
    assertEquals(date(2014, 2, 31), getQuaterEnd(date(2014, 0, 15)));
    assertEquals(date(2014, 2, 31), getQuaterEnd(date(2014, 2, 30)));
    assertEquals(date(2014, 5, 30), getQuaterEnd(date(2014, 4, 15)));
    assertEquals(date(2014, 8, 30), getQuaterEnd(date(2014, 8, 30)));
    assertEquals(date(2014, 11, 31), getQuaterEnd(date(2014, 11, 31)));
  }
}
