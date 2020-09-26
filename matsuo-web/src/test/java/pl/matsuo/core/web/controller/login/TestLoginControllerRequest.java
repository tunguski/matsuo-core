package pl.matsuo.core.web.controller.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.NestedServletException;
import pl.matsuo.core.conf.TestMailConfig;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.login.CreateAccountData;
import pl.matsuo.core.service.login.LoginData;
import pl.matsuo.core.service.login.LoginService;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.web.controller.AbstractDbControllerRequestTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    classes = {
      LoginController.class,
      LoginService.class,
      PermissionService.class,
      TestMailConfig.class
    })
public class TestLoginControllerRequest extends AbstractDbControllerRequestTest {

  @Autowired LoginController controller;

  @Test
  public void testActivateAccount() throws Exception {
    CreateAccountData createAccountData = new CreateAccountData();
    createAccountData.setUsername("kryspin");
    createAccountData.setPassword("kryspinx");
    createAccountData.setCompanyName("kryspin");
    createAccountData.setCompanyShortName("kryspin");
    createAccountData.setCompanyNip("692-000-00-13");

    performAndCheck(
        post("/login/createAccount", createAccountData), http -> assertFalse(http.isEmpty()));

    User user = database.findOne(query(User.class, eq(User::getUsername, "kryspin")));

    try {
      performAndCheckStatus(
          get("/login/activateAccount/" + user.getUnblockTicket()), status().isFound());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    user = database.findOne(query(User.class, eq(User::getUsername, "kryspin")));
    assertFalse(user.getBlocked());
    assertNull(user.getUnblockTicket());

    try {
      performAndCheck(get("/login/activateAccount/" + user.getUnblockTicket()));
    } catch (NestedServletException e) {
      assertTrue(e.getCause() instanceof RestProcessingException);

      LoginData loginData = new LoginData();
      loginData.setUsername("kryspin");
      loginData.setPassword("kryspinx");

      performAndCheck(post("/login", loginData), reponse -> assertEquals(reponse, "\"kryspin\""));

      return;
    }

    fail("Should not come here");
  }
}
