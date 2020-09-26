package pl.matsuo.core.web.mvc;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.Test;

public class TestCustomDateFormat {

  @Test
  public void testParse() throws ParseException {
    CustomDateFormat format = new CustomDateFormat();

    assertEquals(format.parse("2004-02-12T15:19:21+00:00").getTime(), 1076599161000L);
    assertEquals(
        format.parse("2004-02-12T15:19:21+00:00", new ParsePosition(0)).getTime(), 1076599161000L);
  }
}
