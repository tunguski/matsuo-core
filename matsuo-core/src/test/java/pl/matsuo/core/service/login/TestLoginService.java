package pl.matsuo.core.service.login;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import javax.mail.internet.InternetAddress;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {LoginService.class, TestMailConfig.class, GeneralConfig.class})
public class TestLoginService extends AbstractDbTest {

  @Autowired LoginService loginService;
  @Autowired IMailService mailService;

  @Test
  public void testLogin() {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("6%86P#WnukNp2gBm");
    assertEquals("admin", loginService.login(loginData));

    verifyNoMoreInteractions(mailService);
  }

  @Test(expected = UnauthorizedException.class)
  public void testIncorrectLogin() {
    LoginData loginData = new LoginData();
    loginData.setUsername("admin");
    loginData.setPassword("xxx");
    loginService.login(loginData);
  }

  @Test
  public void testActivateAccount() {
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
  public void testRemindPassword() {
    loginService.remindPassword("admin");

    verify(mailService)
        .sendMail(
            any(InternetAddress.class),
            any(InternetAddress.class),
            anyString(),
            anyString(),
            any(Object.class));
    reset(mailService);
  }

  @Test
  public void testCreateAccount() {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("blicky");
    createAccountData.setCompanyName("test");
    createAccountData.setPassword("blickyPass");

    when(mailService.sendMail(
            any(InternetAddress.class),
            any(InternetAddress.class),
            anyString(),
            anyString(),
            any(Object.class)))
        .then(
            invocation -> {
              InternetAddress address = invocation.getArgument(1);
              assertEquals("blicky", address.getAddress());
              return null;
            });

    String ticket = loginService.createAccount(createAccountData, true);
    User blicky = database.findOne(query(User.class, eq(User::getUsername, "blicky")));
    assertEquals(ticket, blicky.getUnblockTicket());

    verify(mailService)
        .sendMail(
            any(InternetAddress.class),
            any(InternetAddress.class),
            anyString(),
            anyString(),
            any(Object.class));
    // clear context for other tests
    reset(mailService);
  }
}
