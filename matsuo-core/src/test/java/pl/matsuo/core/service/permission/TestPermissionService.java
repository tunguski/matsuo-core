package pl.matsuo.core.service.permission;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.user.GroupEnum.*;
import static pl.matsuo.core.service.permission.IPermissionService.RequestType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.permission.model.Permissions;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PermissionService.class, TestSessionState.class})
public class TestPermissionService {

  @Autowired PermissionService permissionService;
  @Autowired SessionState sessionState;

  @Test
  public void testGetPermissions() throws Exception {
    Permissions permissions = permissionService.getPermissions();
    assertNotNull(permissions);
    assertNotNull(permissions.getPermissions());
    assertFalse(permissions.getPermissions().isEmpty());
  }

  protected void configureSessionState(String groupName) {
    User user = new User();
    Group group = new Group();
    group.setName(groupName);
    user.getGroups().add(group);

    sessionState.setUser(user);
  }

  @Test
  public void testAdminPermissions() {
    configureSessionState(ADMIN.name());

    assertTrue(permissionService.isPermitted("addAppointment"));
    assertTrue(permissionService.isPermitted("/reckonings/simple_test"));
    assertTrue(permissionService.isPermitted("xxx_yyy"));
  }

  @Test
  public void testPermissions() {
    configureSessionState("reckonings");

    assertFalse(permissionService.isPermitted("addAppointment"));
    assertTrue(permissionService.isPermitted("/reckonings/simple_test"));
    assertTrue(permissionService.isPermitted("/reckonings"));
    assertFalse(permissionService.isPermitted("xxx_yyy"));
  }

  @Test
  public void testPermissionsWithRestrictedRequestType() {
    configureSessionState("reckonings");

    assertFalse(permissionService.isPermitted("/reckonings/simple_test", POST));
    assertFalse(permissionService.isPermitted("xxx_yyy", POST));
  }

  @Test
  public void testPermissionMatching() {
    permissionService.matches("/reckonings/simple_test", "/reckonings/*");
  }

  @Test
  public void testPermissionResetAfterTime() {
    configureSessionState("reckonings");

    sessionState.setLastRequestTime(0);

    assertFalse(permissionService.isPermitted("addAppointment"));
    assertFalse(permissionService.isPermitted("/reckonings/simple_test"));
    assertFalse(permissionService.isPermitted("xxx_yyy"));

    assertNull(sessionState.getUser());
    assertTrue(sessionState.isInGroup(GUEST.name()));
  }
}
