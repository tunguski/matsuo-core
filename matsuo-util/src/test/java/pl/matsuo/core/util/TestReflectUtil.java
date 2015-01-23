package pl.matsuo.core.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.ReflectUtil.*;

public class TestReflectUtil {


  @Test
  public void testResolveType() throws Exception {
    assertEquals(Integer.class, resolveType(new ArrayList<Integer>() {
    }.getClass(), List.class, 0));
  }

  @Test
  public void testGetValue() throws Exception {
    assertEquals("test X", getValue(new X(), "fieldValue"));
    assertEquals("test X2", getValue(new X(), "methodValue"));

    assertEquals("error", getValue(new Y(), "fieldValue"));
    assertEquals("error 2", getValue(new Y(), "methodValue"));
    assertEquals("test Y", getValue(new Y(), "fieldValue2"));
    assertEquals("test Y2", getValue(new Y(), "methodValue2"));
  }

  @Test
  public void testGetValue1() throws Exception {
    assertEquals("test X", getValue(new Y(), "fieldValue", X.class).get());
    assertEquals("test X2", getValue(new Y(), "methodValue", X.class).get());
  }

  @Test
  public void testGetExactPropertyType() throws Exception {

  }

  @Test
  public void testGetExactPropertyType1() throws Exception {

  }

  @Test
  public void testGetPropertyType() throws Exception {

  }

  @Test
  public void testGetExactAnnotatedElement() throws Exception {

  }

  @Test
  public void testGetExactAnnoatedElement() throws Exception {

  }

  @Test
  public void testGetAnnoatedElement() throws Exception {

  }

  @Test
  public void testInvoke() throws Exception {

  }

  @Test
  public void testFieldName() throws Exception {

  }

  class X {
    private final String fieldValue = "test X";
    private String getMethodValue() {
      return "test X2";
    }
  }


  class Y extends X {
    private final String fieldValue2 = "test Y";
    private String getMethodValue2() {
      return "test Y2";
    }
    private final String fieldValue = "error";
    private String getMethodValue() {
      return "error 2";
    }
  }
}

