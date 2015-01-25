package pl.matsuo.core.util;

import org.junit.Test;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static org.junit.Assert.*;
import static pl.matsuo.core.util.DateUtil.*;


/**
 * Created by marek on 10.04.14.
 */
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
  public void testDate1() throws Exception {
    Date date = date(new Date(100, 1, 2), 10, 20);
    assertEquals(100, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(10, date.getHours());
    assertEquals(20, date.getMinutes());
  }


  @Test
  public void testDate2() throws Exception {
    Date date = date(2020, 1, 2);
    assertEquals(120, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(0, date.getHours());
    assertEquals(0, date.getMinutes());
  }


  @Test
  public void testMaybeDate() throws Exception {
    assertNull(maybeDate("test"));
    assertNotNull(maybeDate("2020-01-02T15:19:21+00:00"));
  }


  @Test
  public void testDate3() throws Exception {
    Date date = date(2020, 1, 2, 8, 25);
    assertEquals(120, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(8, date.getHours());
    assertEquals(25, date.getMinutes());
  }


  @Test
  public void testDate4() throws Exception {
    Date date = date("2020-01-02T15:19:21+00:00");
    assertEquals(120, date.getYear());
    assertEquals(0, date.getMonth());
    assertEquals(2, date.getDate());
    assertEquals(16, date.getHours());
    assertEquals(19, date.getMinutes());
  }


  @Test
  public void testAddTime() throws Exception {
    Date date = addTime(date(2000, 7, 10), MINUTE, 17);
    assertEquals(0, date.getHours());
    assertEquals(17, date.getMinutes());
  }


  @Test
  public void testCal() throws Exception {
    Calendar cal = cal(2015, 4, 22, 10, 20);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }


  @Test
  public void testCal1() throws Exception {
    Calendar cal = cal(2015, 4, 22);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(0, cal.get(HOUR));
    assertEquals(0, cal.get(MINUTE));
  }


  @Test
  public void testCal2() throws Exception {
    Calendar cal = cal(date(2015, 4, 22, 10, 20));
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }


  @Test
  public void testCal3() throws Exception {
    Calendar cal = cal(date(2015, 4, 22, 10, 20).getTime());
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }


  @Test
  public void testCal4() throws Exception {
    Calendar cal = cal(date(2015, 4, 22), 10, 20);
    assertEquals(2015, cal.get(YEAR));
    assertEquals(4, cal.get(MONTH));
    assertEquals(22, cal.get(DAY_OF_MONTH));
    assertEquals(10, cal.get(HOUR));
    assertEquals(20, cal.get(MINUTE));
  }


  @Test
  public void testTime() throws Exception {
    Time time = time(10, 20);
    assertEquals(10, time.getHours());
    assertEquals(20, time.getMinutes());
  }


  @Test
  public void testDateAndTime() throws Exception {
    Date date = dateAndTime(date(2015, 4, 22), time(10, 20));
    assertEquals(115, date.getYear());
    assertEquals(4, date.getMonth());
    assertEquals(22, date.getDate());
    assertEquals(10, date.getHours());
    assertEquals(20, date.getMinutes());
  }


  @Test
  public void testMin() throws Exception {
    assertEquals(date(2014, 7, 12), min(date(2015, 1, 7), date(2014, 7, 12)));
  }


  @Test
  public void testMax() throws Exception {
    assertEquals(date(2015, 1, 7), max(date(2015, 1, 7), date(2014, 7, 12)));
  }


  @Test
  public void testMin1() throws Exception {
    assertEquals(cal(2014, 7, 12), min(cal(2015, 1, 7), cal(2014, 7, 12)));
  }


  @Test
  public void testMax1() throws Exception {
    assertEquals(cal(2015, 1, 7), max(cal(2015, 1, 7), cal(2014, 7, 12)));
  }


  @Test
  public void testBetween() throws Exception {
    assertTrue(between(date(2014, 9, 12), date(2014, 7, 12), date(2015, 1, 7)));
    assertFalse(between(date(2012, 9, 12), date(2014, 7, 12), date(2015, 1, 7)));
  }


  @Test
  public void testSqlDate() throws Exception {
  }


  @Test
  public void testSqlDate1() throws Exception {
  }


  @Test
  public void testSqlDate2() throws Exception {
  }


  @Test
  public void testSqlDate3() throws Exception {
  }


  @Test
  public void testIsoFormat1() throws Exception {
  }


  @Test
  public void testLocalDate() throws Exception {
  }


  @Test
  public void testGetQuaterStart() throws Exception {
    assertEquals(date(2014, 0, 1), getQuaterStart(date(2014, 0, 15)));
    assertEquals(date(2014, 0, 1), getQuaterStart(date(2014, 2, 30)));
    assertEquals(date(2014, 3, 1), getQuaterStart(date(2014, 4, 15)));
    assertEquals(date(2014, 6, 1), getQuaterStart(date(2014, 8, 30)));
    assertEquals(date(2014, 9, 1), getQuaterStart(date(2014, 11, 31)));
  }


  @Test
  public void testGetQuaterEnd() throws Exception {
    assertEquals(date(2014, 2, 31), getQuaterEnd(date(2014, 0, 15)));
    assertEquals(date(2014, 2, 31), getQuaterEnd(date(2014, 2, 30)));
    assertEquals(date(2014, 5, 30), getQuaterEnd(date(2014, 4, 15)));
    assertEquals(date(2014, 8, 30), getQuaterEnd(date(2014, 8, 30)));
    assertEquals(date(2014, 11, 31), getQuaterEnd(date(2014, 11, 31)));
  }
}


