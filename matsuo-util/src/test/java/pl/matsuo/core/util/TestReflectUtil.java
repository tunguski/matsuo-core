package pl.matsuo.core.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.ReflectUtil.*;

public class TestReflectUtil {


  class X {
    private final String fieldValue = "test";
    private String getMethodValue() {
      return "test2";
    }
  }


  class Y extends X {
    private final String fieldValue2 = "test";
    private String getMethodValue2() {
      return "test2";
    }
  }


  @Test
  public void testReflectUtil() throws Exception {
    assertEquals("test", getValue(new X(), "fieldValue"));
    assertEquals("test2", getValue(new X(), "methodValue"));

    assertEquals("test", getValue(new Y(), "fieldValue"));
    assertEquals("test2", getValue(new Y(), "methodValue"));
    assertEquals("test", getValue(new Y(), "fieldValue2"));
    assertEquals("test2", getValue(new Y(), "methodValue2"));
  }
}

