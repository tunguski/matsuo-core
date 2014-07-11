package pl.matsuo.core.web.controller.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;
import pl.matsuo.core.test.data.UserTestData;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.controller.exception.RestProcessingException;
import pl.matsuo.core.web.mvc.MvcConfig;

import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.model.user.GroupEnum.*;


/**
 * Created by tunguski on 15.01.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { LoginController.class, PermissionService.class })
public class TestLoginController extends AbstractControllerTest {
  private static final Logger logger = LoggerFactory.getLogger(TestLoginController.class);


  @Autowired
  LoginController controller;


  @Test
  public void testLoggedUsername() throws Exception {
    User user = new User();
    user.getGroups().add(new Group());
    sessionState.setUser(user);
    sessionState.getUser().setUsername("test_1");

    assertEquals("test_1", controller.loggedUsername());
  }


  @Test
  public void testCorrectLogin() throws Exception {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("test");

    assertEquals("admin", controller.login(loginData));

    assertNotNull(sessionState.getUser());
    assertEquals("admin", sessionState.getUser().getUsername());
  }


  @Test(expected = LoginController.UnauthorizedException.class)
  public void testFalseLogin() throws Exception {
    LoginData loginData = new LoginData();
    loginData.setUsername("test");
    loginData.setPassword("test");

    controller.login(loginData);
  }


  @Test
  public void testLogoff() throws Exception {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    controller.logoff();


    assertNull(sessionState.getUser());
    assertTrue(sessionState.isInGroup(GUEST.name()));
  }


  @Test
  public void testLoggedUser() throws Exception {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    assertTrue(sessionState.getUser().equals(controller.loggedUser()));
  }


  @Test
  public void testLoginTime() throws Exception {
    // zapewnienie autoryzacji przed właściwym testem
    testCorrectLogin();

    assertTrue(sessionState.getLastRequestTime() == sessionState.getLoginTime());
  }


  @Test
  public void testActivateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("tristan");
    createAccountData.setPassword("tristan");
    createAccountData.setCompanyName("tristan");
    createAccountData.setCompanyShortName("tristan");
    createAccountData.setCompanyNip("692-000-00-13");

    String result = controller.createAccount(createAccountData);
    logger.info(result);
    assertFalse(result.isEmpty());

    User user = database.findOne(query(User.class, eq("username", "tristan")));

    try {
      controller.activateAccount(user.getUnblockTicket());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    user = database.findOne(query(User.class, eq("username", "tristan")));
    assertFalse(user.getBlocked());
    assertNull(user.getUnblockTicket());
    assertNotNull(user.getIdBucket());

    checkEntitiesCountForBucket(user.getIdBucket(), 1, OrganizationUnit.class);
    checkEntitiesCountForBucket(user.getIdBucket(), 1, Person.class);
    checkEntitiesCountForBucket(user.getIdBucket(), 1, Address.class);

    try {
      controller.activateAccount(user.getUnblockTicket());
    } catch (Exception e) {
      return;
    }

    fail();
  }


  protected void checkEntitiesCountForBucket(Integer idBucket, Integer size, Class<? extends AbstractEntity> clazz) {
    List<AbstractEntity> entities =
        database.find(query((Class<AbstractEntity>) clazz, eq("idBucket", idBucket)));
    assertEquals(1, entities.size());
  }


  @Test(expected = RestProcessingException.class)
  public void testCreateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("kryspin");
    createAccountData.setPassword("kryspin");
    createAccountData.setCompanyName("kryspin");
    createAccountData.setCompanyShortName("kryspin");
    createAccountData.setCompanyNip("692-000-00-13");

    String result = controller.createAccount(createAccountData);
    logger.info(result);
    assertFalse(result.isEmpty());

    controller.createAccount(createAccountData);
  }


  @Test
  public void testRemindPassword() throws Exception {
    // FIXME: test przesłania hasła poprzez maila - wywołanie odpowiedniej funkcji z parametrami
  }
}

