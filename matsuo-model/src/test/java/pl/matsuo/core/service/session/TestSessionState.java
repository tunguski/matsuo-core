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
  public void testSetUser() {
    SessionState sessionState = new SessionState();

    User user = new User();
    user.setIdBucket(11L);

    sessionState.setUser(user);

    assertEquals((Long) 11L, user.getIdBucket());
    assertTrue(sessionState.isInGroup(USER.name()));
    assertFalse(sessionState.isInGroup(GUEST.name()));
  }

  @Test
  public void testGuestUser() {
    SessionState sessionState = new SessionState();

    assertFalse(sessionState.isInGroup(USER.name()));
    assertTrue(sessionState.isInGroup(GUEST.name()));
  }
}
