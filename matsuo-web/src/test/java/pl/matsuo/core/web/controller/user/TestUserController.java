package pl.matsuo.core.web.controller.user;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.SecurityUtil.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.web.controller.AbstractControllerTest;

@ContextConfiguration(classes = {UserController.class})
public class TestUserController extends AbstractControllerTest {

  @Autowired protected UserController userController;

  protected User user;

  @Before
  public void setup() {
    user = database.findAll(User.class, new UserInitializer()).get(0);
  }

  @Test
  public void testCreateUser() throws Exception {
    Person person = new Person();
    person.setFirstName("Ryszard");
    person.setLastName("Monty");

    User user = new User();
    user.setUsername("username");
    user.setPassword("password");
    user.setPerson(person);

    Integer idUser = idFromLocation(userController.create(user, new StringBuffer("")));

    user = database.findById(User.class, idUser);

    assertEquals("Ryszard", user.getPerson().getFirstName());
    assertEquals("Monty", user.getPerson().getLastName());
    assertEquals("username", user.getUsername());
    assertEquals(passwordHash("password"), user.getPassword());
  }

  @Test(expected = RestProcessingException.class)
  public void testUpdatePassword_PasswordTooShort() throws Exception {
    IChangePasswordParams params =
        facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setNewPassword("aaa");
    params.setConfirmationPassword("bbb");

    userController.updatePassword(params);
  }

  @Test(expected = RestProcessingException.class)
  public void testUpdatePassword_ConfirmationMissmatch() throws Exception {
    IChangePasswordParams params =
        facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setNewPassword("aaafdsfdafdafds");
    params.setConfirmationPassword("bbbfdsfdfafdfadf");

    userController.updatePassword(params);
  }

  @Test(expected = RestProcessingException.class)
  public void testUpdateOwnPassword_WrongPassword() throws Exception {
    sessionState.setUser(user);

    IChangePasswordParams params =
        facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setActualPassword("test__");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updateOwnPassword(params);
  }

  @Test
  @DirtiesContext
  public void testUpdatePassword() throws Exception {
    IChangePasswordParams params =
        facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(user.getId());
    params.setActualPassword("6%86P#WnukNp2gBm");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updatePassword(params);
  }

  @Test
  @DirtiesContext
  public void testUpdateOwnPassword() throws Exception {
    sessionState.setUser(user);

    IChangePasswordParams params =
        facadeBuilder.createFacade(new HashMap<>(), IChangePasswordParams.class);

    params.setId(0);
    params.setActualPassword("6%86P#WnukNp2gBm");
    params.setNewPassword("kredka111");
    params.setConfirmationPassword("kredka111");

    userController.updateOwnPassword(params);

    assertEquals(
        passwordHash("kredka111"), database.findById(User.class, user.getId()).getPassword());
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
