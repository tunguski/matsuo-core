package pl.matsuo.core.util;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.matsuo.core.util.ReflectUtil.fieldName;
import static pl.matsuo.core.util.ReflectUtil.getAnnotatedElement;
import static pl.matsuo.core.util.ReflectUtil.getExactAnnotatedElement;
import static pl.matsuo.core.util.ReflectUtil.getExactPropertyType;
import static pl.matsuo.core.util.ReflectUtil.getPropertyType;
import static pl.matsuo.core.util.ReflectUtil.getValue;
import static pl.matsuo.core.util.ReflectUtil.invoke;
import static pl.matsuo.core.util.ReflectUtil.resolveType;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TestReflectUtil {

  @Test
  public void testResolveType() throws Exception {
    assertEquals(Integer.class, resolveType(new ArrayList<Integer>() {}.getClass(), List.class, 0));
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
    assertEquals(String.class, getExactPropertyType(X.class, "fieldValue"));
  }

  @Test
  public void testGetExactPropertyType1() throws Exception {
    assertEquals(String.class, getExactPropertyType(asList(X.class), "fieldValue"));
  }

  @Test
  public void testGetPropertyType() throws Exception {
    assertEquals(String.class, getPropertyType(Z.class, "complex.fieldValue"));
    assertEquals(Z.class, getPropertyType(Z.class, "complex.complex"));
  }

  @Test(expected = RuntimeException.class)
  public void testGetPropertyType1() throws Exception {
    assertEquals(Z.class, getPropertyType(Z.class, "xxx"));
  }

  @Test(expected = RuntimeException.class)
  public void testGetPropertyType2() throws Exception {
    assertEquals(Z.class, getPropertyType(Z.class, "xxx.xxx"));
  }

  @Test
  public void testGetExactAnnotatedElement() throws Exception {
    AnnotatedElement complex = getExactAnnotatedElement(Z.class, "complex");
    assertNotNull(complex);
    assertEquals(Method.class, complex.getClass());
  }

  @Test
  public void testGetExactAnnoatedElement() throws Exception {
    AnnotatedElement complex = getExactAnnotatedElement(asList(Object.class, Z.class), "complex");
    assertNotNull(complex);
    assertEquals(Method.class, complex.getClass());
  }

  @Test
  public void testGetAnnotatedElement() throws Exception {
    AnnotatedElement complex = getAnnotatedElement(Z.class, "complex");
    assertNotNull(complex);
    assertEquals(Method.class, complex.getClass());
  }

  @Test
  public void testInvoke() throws Exception {
    Z z = new Z();
    assertEquals(0, z.invocations);
    invoke(z, "invoke");
    assertEquals(1, z.invocations);
  }

  @Test
  public void testFieldName() throws Exception {
    assertEquals("string", fieldName("getString"));
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

  class Z extends X {
    public int invocations = 0;

    public void invoke() {
      invocations++;
    }

    private Z getComplex() {
      return new Z();
    }
  }
}
