package pl.matsuo.core.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.SecurityUtil.*;


/**
 * Created by tunguski on 29.01.14.
 */
public class TestSecurityUtil {


  @Test
  public void testPasswordHash() throws Exception {
    assertEquals("CXd0FAUCwX5fjbqPpbVruf5sIXSuGdzrGD1aaDp/qVw=", passwordHash("SHA (Secure Hash Algorithm)"));
    assertEquals("2Et/vS3MOiBxWaHw6Wjug0inBaIiwi316d800tiWjI8=", passwordHash("No software installation needed."));
  }
}
