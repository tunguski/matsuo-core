package pl.matsuo.core.service.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.model.user.GroupEnum.GUEST;
import static pl.matsuo.core.model.user.GroupEnum.USER;

import org.junit.Test;
import pl.matsuo.core.model.user.User;

public class TestSessionState {

  @Test
  public void testSetUser() throws Exception {
    SessionState sessionState = new SessionState();

    User user = new User();
    user.setIdBucket(11);

    sessionState.setUser(user);

    assertEquals((Integer) 11, user.getIdBucket());
    assertTrue(sessionState.isInGroup(USER.name()));
    assertFalse(sessionState.isInGroup(GUEST.name()));
  }

  @Test
  public void testGuestUser() throws Exception {
    SessionState sessionState = new SessionState();

    assertFalse(sessionState.isInGroup(USER.name()));
    assertTrue(sessionState.isInGroup(GUEST.name()));
  }
}
