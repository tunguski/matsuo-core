package pl.matsuo.core.util;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.StringUtil.*;

import org.junit.Test;

public class TestStringUtil {

  @Test
  public void testNotEmpty() throws Exception {
    assertTrue(notEmpty(" f"));
    assertFalse(notEmpty(" "));
  }

  @Test
  public void testCamelCaseToCssName() throws Exception {
    assertEquals("figzy-smally", camelCaseToCssName("FigzySmally"));
  }
}
