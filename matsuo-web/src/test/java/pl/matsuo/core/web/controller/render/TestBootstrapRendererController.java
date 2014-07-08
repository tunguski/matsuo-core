package pl.matsuo.core.web.controller.render;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;


@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = { MvcConfig.class, BootstrapRendererController.class })
public class TestBootstrapRendererController extends AbstractControllerTest {


  @Autowired BootstrapRendererController controller;


  @Test
  public void loadScheduleBasic() {
//    LoadScheduleForm form = createForm();
//
//    ScheduleResult loadSchedule = controller.loadSchedule(form);
//
//    assertEquals(7, loadSchedule.getScheduleElements().size());
//
//    for (ScheduleElement scheduleElement : loadSchedule.getScheduleElements()) {
//      assertEquals((Integer) 12, scheduleElement.getDuration());
//      assertTrue(bd("120").compareTo(scheduleElement.getPrice()) == 0);
//    }
  }
}

