package pl.matsuo.core.web.controller.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.model.user.GroupEnum.GUEST;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.core.conf.TestMailConfig;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.exception.UnauthorizedException;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.login.CreateAccountData;
import pl.matsuo.core.service.login.ILoginServiceExtension;
import pl.matsuo.core.service.login.LoginData;
import pl.matsuo.core.service.login.LoginService;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.web.controller.AbstractControllerTest;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    classes = {
      LoginController.class,
      LoginService.class,
      PermissionService.class,
      TestLoginController.LoginServiceExtension.class,
      TestMailConfig.class
    })
public class TestLoginController extends AbstractControllerTest {

  @Autowired LoginController controller;
  @Autowired TestLoginController.LoginServiceExtension loginServiceExtension;

  @Test
  public void testLoggedUsername() {
    User user = new User();
    user.getGroups().add(new Group());
    sessionState.setUser(user);
    sessionState.getUser().setUsername("test_1");

    assertEquals("test_1", controller.loggedUsername());
  }

  @Test
  public void testCorrectLogin() {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("6%86P#WnukNp2gBm");

    assertEquals("admin", controller.login(loginData));

    assertNotNull(sessionState.getUser());
    assertEquals("admin", sessionState.getUser().getUsername());
  }

  @Test(expected = UnauthorizedException.class)
  public void testFalseLogin() {
    LoginData loginData = new LoginData();
    loginData.setUsername("test");
    loginData.setPassword("test");

    controller.login(loginData);
  }

  @Test
  public void testLogoff() {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    controller.logoff();

    assertNull(sessionState.getUser());
    assertTrue(sessionState.isInGroup(GUEST.name()));
  }

  @Test
  public void testLoggedUser() {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    assertTrue(sessionState.getUser().equals(controller.loggedUser()));
  }

  @Test
  public void testLoginTime() {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    assertTrue(sessionState.getLastRequestTime() == sessionState.getLoginTime());
  }

  @Test
  public void testActivateAccount() {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("tristan");
    createAccountData.setPassword("tristan");
    createAccountData.setCompanyName("tristan");
    createAccountData.setCompanyShortName("tristan");
    createAccountData.setCompanyNip("692-000-00-13");

    String result = controller.createAccount(createAccountData);
    log.info(result);
    assertFalse(result.isEmpty());

    User user = database.findOne(query(User.class, eq(User::getUsername, "tristan")));
    MockHttpServletResponse response = new MockHttpServletResponse();

    try {
      controller.activateAccount(user.getUnblockTicket(), response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    user = database.findOne(query(User.class, eq(User::getUsername, "tristan")));
    assertFalse(user.getBlocked());
    assertNull(user.getUnblockTicket());
    assertNotNull(user.getIdBucket());

    checkEntitiesCountForBucket(user.getIdBucket(), 1, OrganizationUnit.class);
    checkEntitiesCountForBucket(user.getIdBucket(), 1, Person.class);
    checkEntitiesCountForBucket(user.getIdBucket(), 1, Address.class);

    try {
      response = new MockHttpServletResponse();
      controller.activateAccount(user.getUnblockTicket(), response);
    } catch (Exception e) {
      return;
    }

    fail();
  }

  protected void checkEntitiesCountForBucket(
      Long idBucket, Integer size, Class<? extends AbstractEntity> clazz) {
    List<AbstractEntity> entities =
        database.find(
            query((Class<AbstractEntity>) clazz, eq(AbstractEntity::getIdBucket, idBucket)));
    assertEquals(1, entities.size());
  }

  @Test
  public void testCreateAccount() {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("kryspin");
    createAccountData.setPassword("kryspin");
    createAccountData.setCompanyName("kryspin");
    createAccountData.setCompanyShortName("kryspin");
    createAccountData.setCompanyNip("692-000-00-13");

    loginServiceExtension.counter = 0;

    String result = controller.createAccount(createAccountData);
    log.info(result);
    assertFalse(result.isEmpty());
    assertEquals(1, loginServiceExtension.counter);

    try {
      controller.createAccount(createAccountData);
    } catch (RestProcessingException e) {
      return;
    }

    fail();
  }

  static class LoginServiceExtension implements ILoginServiceExtension {

    public int counter = 0;

    @Override
    public void createAccount(OrganizationUnit organizationUnit, User user) {
      counter++;
    }
  }

  @Test
  public void testRemindPassword() {
    // FIXME: test przesłania hasła poprzez maila - wywołanie odpowiedniej funkcji z parametrami
  }
}
