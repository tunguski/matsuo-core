package pl.matsuo.core.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.StringUtil.*;


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

