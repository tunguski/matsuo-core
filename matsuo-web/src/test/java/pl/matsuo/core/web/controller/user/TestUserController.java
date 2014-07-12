package pl.matsuo.core.web.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.exception.RestProcessingException;

import java.util.HashMap;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.SecurityUtil.*;


@ContextConfiguration(classes = { UserController.class })
public class TestUserController extends AbstractControllerTest {


  @Autowired
  protected UserController userController;


  protected User user;


  @Before
  public void setup() {
    user = database.findAll(User.class, new UserInitializer()).get(0);
  }


  @Test(expected = RestProcessingException.class)
  public void testUpdatePassword_PasswordTooShort() throws Exception {
    IChangePasswordParams params = facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setNewPassword("aaa");
    params.setConfirmationPassword("bbb");

    userController.updatePassword(params);
  }


  @Test(expected = RestProcessingException.class)
  public void testUpdatePassword_ConfirmationMissmatch() throws Exception {
    IChangePasswordParams params = facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setNewPassword("aaafdsfdafdafds");
    params.setConfirmationPassword("bbbfdsfdfafdfadf");

    userController.updatePassword(params);
  }


  @Test(expected = RestProcessingException.class)
  public void testUpdateOwnPassword_WrongPassword() throws Exception {
    sessionState.setUser(user);

    IChangePasswordParams params = facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setActualPassword("test__");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updateOwnPassword(params);
  }


  @Test
  @DirtiesContext
  public void testUpdatePassword() throws Exception {
    IChangePasswordParams params = facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setActualPassword("test");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updatePassword(params);
  }


  @Test
  @DirtiesContext
  public void testUpdateOwnPassword() throws Exception {
    sessionState.setUser(user);

    IChangePasswordParams params = facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(0);
    params.setActualPassword("test");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updateOwnPassword(params);

    assertEquals(passwordHash("kredka111"), database.findById(User.class, user.getId()).getPassword());
  }


  @Test
  @DirtiesContext
  public void testBlockUser() throws Exception {
    IBlockUserParams params = facadeBuilder.createFacade(new HashMap<>(), IBlockUserParams.class);

    params.setId(user.getId());
    params.setBlock(true);

    userController.blockUser(params);

    assertTrue(database.findById(User.class, user.getId()).getBlocked());

    params.setBlock(false);

    userController.blockUser(params);

    assertFalse(database.findById(User.class, user.getId()).getBlocked());
  }
}

