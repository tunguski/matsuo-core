package pl.matsuo.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.StringUtil.camelCaseToCssName;
import static pl.matsuo.core.util.StringUtil.notEmpty;

import org.junit.Test;

public class TestStringUtil {

  @Test
  public void testNotEmpty() {
    assertTrue(notEmpty(" f"));
    assertFalse(notEmpty(" "));
  }

  @Test
  public void testCamelCaseToCssName() {
    assertEquals("figzy-smally", camelCaseToCssName("FigzySmally"));
  }
}
