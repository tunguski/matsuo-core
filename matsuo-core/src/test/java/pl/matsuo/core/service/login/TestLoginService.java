package pl.matsuo.core.service.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.conf.GeneralConfig;
import pl.matsuo.core.conf.TestMailConfig;
import pl.matsuo.core.exception.UnauthorizedException;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.mail.IMailService;

import javax.mail.internet.InternetAddress;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LoginService.class, TestMailConfig.class, GeneralConfig.class })
public class TestLoginService extends AbstractDbTest {


  @Autowired
  LoginService loginService;
  @Autowired
  IMailService mailService;


  @Test
  public void testLogin() throws Exception {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("test");
    assertEquals("admin", loginService.login(loginData));

    verifyNoMoreInteractions(mailService);
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
    String ticket = loginService.createAccount(createAccountData, false);

    verifyNoMoreInteractions(mailService);

    loginService.activateAccount(ticket);

    LoginData loginData = new LoginData();
    loginData.setUsername("blicky2");
    loginData.setPassword("blickyPass2");
    assertEquals("blicky2", loginService.login(loginData));
  }


  @Test
  public void testRemindPassword() throws Exception {
    loginService.remindPassword("admin");

    verify(mailService).sendMail(any(InternetAddress.class), any(InternetAddress.class), anyString(), anyString(), anyObject());
    reset(mailService);
  }


  @Test
  public void testCreateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("blicky");
    createAccountData.setCompanyName("test");
    createAccountData.setPassword("blickyPass");

    when(mailService.sendMail(any(InternetAddress.class), any(InternetAddress.class), anyString(), anyString(), anyObject())).then(
        invocation -> {
          InternetAddress address = invocation.getArgumentAt(1, InternetAddress.class);
          assertEquals("blicky", address.getAddress());
          return null;
        });


    String ticket = loginService.createAccount(createAccountData, true);
    User blicky = database.findOne(query(User.class, eq(User::getUsername, "blicky")));
    assertEquals(ticket, blicky.getUnblockTicket());

    verify(mailService).sendMail(any(InternetAddress.class), any(InternetAddress.class), anyString(), anyString(), anyObject());
    // clear context for other tests
    reset(mailService);
  }
}

