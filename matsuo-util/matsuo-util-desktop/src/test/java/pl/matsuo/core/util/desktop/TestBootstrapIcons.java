package pl.matsuo.core.util.desktop;

import static org.junit.Assert.assertNotNull;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import pl.matsuo.core.util.desktop.component.BootstrapIcons;

@Slf4j
public class TestBootstrapIcons {

  @Test
  public void text() {
    String noAdditionalClasses = BootstrapIcons.alarm_fill.text();
    log.info(noAdditionalClasses);
    assertNotNull(noAdditionalClasses);

    String withAdditionalClasses = BootstrapIcons.alarm_fill.text("one", "two");
    log.info(withAdditionalClasses);
    assertNotNull(withAdditionalClasses);
  }
}
