package pl.matsuo.core.service.i18n;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.GeneralConfig;

/** Created by tunguski on 26.09.13. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {I18nServiceImpl.class, GeneralConfig.class})
public class TestI18nServiceImpl {

  @Autowired I18nService i18nService;

  @Test
  public void testTranslation() {
    //    assertEquals("Ortopedia i traumatologia narządu ruchu",
    //                    i18nService.translate("Ortopedia_i_traumatologia_narzadu_ruchu"));
  }
}
