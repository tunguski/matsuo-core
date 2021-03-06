package pl.matsuo.core.util.function;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.function.FunctionalUtil.AccessProvider;
import static pl.matsuo.core.util.function.FunctionalUtil.access;
import static pl.matsuo.core.util.function.FunctionalUtil.collectList;
import static pl.matsuo.core.util.function.FunctionalUtil.compose;
import static pl.matsuo.core.util.function.FunctionalUtil.either;
import static pl.matsuo.core.util.function.FunctionalUtil.ignoreEx;
import static pl.matsuo.core.util.function.FunctionalUtil.processEx;
import static pl.matsuo.core.util.function.FunctionalUtil.repeat;
import static pl.matsuo.core.util.function.FunctionalUtil.runtimeEx;
import static pl.matsuo.core.util.function.FunctionalUtil.transform;
import static pl.matsuo.core.util.function.FunctionalUtil.with;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

public class TestFunctionalUtil {

  @Test
  public void testAccess() throws NoSuchFieldException {
    AccessProvider<Integer> invocations = access(new Z(), Z.class.getField("invocations"));
    assertEquals((Integer) 0, invocations.get());
    invocations.accept(7);
    assertEquals((Integer) 7, invocations.get());
  }

  @Test
  public void testAccess1() {
    Z z = new Z();
    AccessProvider<Z> access = access(z::getComplex, z::setComplex);
    assertNull(access.get());
    access.accept(z);
    assertEquals(z, access.get());
  }

  @Test
  public void testWith() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    with(
        "string",
        value -> {
          assertEquals("string", value);
          invoked.set(true);
        });
    assertTrue(invoked.get());
  }

  @Test
  public void testTransform() {
    assertEquals((Integer) 7, transform("string", value -> value.equals("string") ? 7 : null));
  }

  @Test
  public void testWith1() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    with(
        "string",
        "string2",
        (v1, v2) -> {
          assertEquals("string", v1);
          assertEquals("string2", v2);
          invoked.set(true);
        });
    assertTrue(invoked.get());
  }

  @Test
  public void testIgnoreEx() {
    ignoreEx(
        () -> {
          throw new RuntimeException("Should be ignored");
        });
  }

  @Test
  public void testIgnoreEx1() {
    assertEquals("val", ignoreEx(() -> "val"));
    assertEquals(
        (Object) null,
        ignoreEx(
            () -> {
              throw new RuntimeException("Should be ignored");
            }));
  }

  @Test(expected = RuntimeException.class)
  public void testRuntimeEx() {
    runtimeEx(
        () -> {
          throw new Exception("Should be wrapped into RuntimeException");
        });
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRuntimeEx1() {
    runtimeEx(
        () -> {
          throw new Exception("Should be wrapped into RuntimeException");
        },
        ex -> {
          throw new IllegalArgumentException(ex);
        });
  }

  @Test
  public void testRuntimeEx2() {
    assertEquals("val", runtimeEx(() -> "val"));
  }

  @Test(expected = RuntimeException.class)
  public void testRuntimeEx3() {
    assertEquals(
        "val",
        runtimeEx(
            () -> {
              throw new Exception();
            }));
  }

  @Test
  public void testProcessEx() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    processEx(
        () -> {
          throw new Exception("Should be wrapped into RuntimeException");
        },
        ex -> {
          invoked.set(true);
        });
    assertTrue(invoked.get());
  }

  @Test
  public void testProcessEx1() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    assertEquals(
        "y",
        processEx(
            () -> {
              throw new Exception("Should be wrapped into RuntimeException");
            },
            ex -> {
              invoked.set(true);
              return "y";
            }));
    assertTrue(invoked.get());
  }

  @Test
  public void testProcessEx2() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    assertEquals(
        "x",
        processEx(
            () -> "x",
            ex -> {
              invoked.set(true);
              return "y";
            }));
    assertFalse(invoked.get());
  }

  @Test
  public void testCompose() {
    AtomicBoolean invoked1 = new AtomicBoolean(false);
    AtomicBoolean invoked2 = new AtomicBoolean(false);

    compose(val -> invoked1.set(true), val -> invoked2.set(true)).accept("val");

    assertTrue(invoked1.get());
    assertTrue(invoked2.get());
  }

  @Test
  public void testEither() {
    assertEquals((Integer) 1, either(true, () -> 1, () -> 2));
    assertEquals((Integer) 2, either(false, () -> 1, () -> 2));
  }

  @Test
  public void testRepeat() {
    AtomicInteger counter = new AtomicInteger(3);
    AtomicInteger value = new AtomicInteger(0);

    repeat(
        () -> {
          counter.set(counter.get() - 1);
          return counter.get() >= 0;
        },
        () -> value.set(value.get() + 1));

    assertEquals(-1, counter.get());
    assertEquals(3, value.get());
  }

  @Test
  public void testCollectList() {
    AtomicInteger counter = new AtomicInteger(3);

    List<Integer> integers =
        collectList(
            () -> {
              counter.set(counter.get() - 1);
              return counter.get() >= 0;
            },
            () -> counter.get());

    assertEquals(-1, counter.get());
    assertEquals(asList(2, 1, 0), integers);
  }

  @Getter
  @Setter
  class Z {
    public int invocations = 0;
    public Z complex;
  }
}
