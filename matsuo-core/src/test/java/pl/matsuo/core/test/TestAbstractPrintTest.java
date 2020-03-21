package pl.matsuo.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

/** Created by marek on 20.03.14. */
public class TestAbstractPrintTest {

  @Test
  public void testLookupTestName() throws Exception {
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
