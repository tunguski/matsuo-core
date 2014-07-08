package pl.matsuo.core.util;

import org.junit.Test;

import java.util.Calendar;

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
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(1398170096789L);
    assertEquals("2014-04-22T12:34:56Z", isoFormat(cal.getTime()));
  }
}

