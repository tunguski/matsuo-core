package pl.matsuo.core.web.controller.print;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAbstractPrintController {

  @Test
  public void test() {
    AbstractPrintController controller = new AbstractPrintController() {};

    assertEquals(1, controller.entityInitializers().size());
  }
}
