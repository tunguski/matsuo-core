package pl.matsuo.core.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestAbstractPrintTest {

  @Test
  public void testLookupTestName() {
    AbstractPrintTest abstractPrintTest =
        new AbstractPrintTest() {
          @Override
          protected String getPrintFileName() {
            return "/prints/test.ftl";
          }
        };

    assertEquals("testLookupTestName", abstractPrintTest.lookupTestName());
  }
}
