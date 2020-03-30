package pl.matsuo.core.web.scope;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.service.session.SessionState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SessionState.class, ScopeConfig.class})
public class TestWideSessionScope {

  @Autowired SessionState sessionState;

  @Test
  public void testAutowiringOutsideWebRequest() throws Exception {
    sessionState.getIdBucket();
  }

  @Test
  public void testWideSessionScope() {
    WideSessionScope wideSessionScope = new WideSessionScope();

    assertEquals("non_web_", wideSessionScope.getConversationId());
    assertEquals(13, wideSessionScope.getScope());

    ThreadLocal<Object> threadLocal = new ThreadLocal<>();
    threadLocal.set("test object");
    wideSessionScope.objectHolders.put("object", threadLocal);

    assertEquals("test object", wideSessionScope.remove("object"));
  }
}
