package pl.matsuo.core.web.controller.print;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestAbstractPrintController {

  @Test
  public void test() {
    AbstractPrintController controller = new AbstractPrintController() {};

    assertEquals(1, controller.entityInitializers().size());
  }
}