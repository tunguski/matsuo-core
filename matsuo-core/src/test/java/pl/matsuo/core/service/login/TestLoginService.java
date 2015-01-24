package pl.matsuo.core.service.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.exception.UnauthorizedException;
import pl.matsuo.core.model.query.QueryBuilder;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.test.data.TestSessionState;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LoginService.class })
public class TestLoginService extends AbstractDbTest {


  @Autowired
  LoginService loginService;


  @Test
  public void testLogin() throws Exception {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("test");
    assertEquals("admin", loginService.login(loginData));
  }


  @Test(expected = UnauthorizedException.class)
  public void testIncorrectLogin() throws Exception {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("xxx");
    loginService.login(loginData);
  }


  @Test
  public void testActivateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("blicky2");
    createAccountData.setCompanyName("test2");
    createAccountData.setPassword("blickyPass2");
    String ticket = loginService.createAccount(createAccountData);

    loginService.activateAccount(ticket);

    LoginData loginData = new LoginData();
    loginData.setUsername("blicky2");
    loginData.setPassword("blickyPass2");
    assertEquals("blicky2", loginService.login(loginData));
  }


  @Test
  public void testCreateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("blicky");
    createAccountData.setCompanyName("test");
    createAccountData.setPassword("blickyPass");
    String ticket = loginService.createAccount(createAccountData);
    User blicky = database.findOne(query(User.class, eq("username", "blicky")));
    assertEquals(ticket, blicky.getUnblockTicket());
  }
}