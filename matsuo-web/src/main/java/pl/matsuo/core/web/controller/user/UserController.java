package pl.matsuo.core.web.controller.user;

import static java.util.Arrays.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.SecurityUtil.*;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.web.controller.AbstractSimpleController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractSimpleController<User> {

  @Autowired protected SessionState sessionState;
  protected Integer minimalPasswordLength = 8;

  @Override
  protected List<Function<User, String>> queryMatchers() {
    return asList(
        User::getUsername,
        sub(User::getPerson, Person::getFirstName),
        sub(User::getPerson, Person::getLastName));
  }

  @Override
  protected List<? extends Initializer<User>> entityInitializers() {
    return asList(new UserInitializer());
  }

  @RequestMapping(
      method = POST,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<User> create(
      @RequestBody @Valid User entity, @Value("#{request.requestURL}") StringBuffer parentUri) {
    Person person = database.create(entity.getPerson());
    entity.setPerson(person);

    if (entity.getPassword().length() < minimalPasswordLength) {
      throw new RestProcessingException("password_too_short");
    }

    entity.setPassword(passwordHash(entity.getPassword()));

    entity = database.create(entity);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(childLocation(parentUri, entity.getId()));
    return new HttpEntity<User>(headers);
  }

  @RequestMapping(
      value = "updatePassword",
      method = PUT,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void updatePassword(@RequestBody IChangePasswordParams changePasswordParams) {
    User user = database.findById(User.class, changePasswordParams.getId());

    if (changePasswordParams.getNewPassword().length() < minimalPasswordLength) {
      throw new RestProcessingException("password_too_short");
    } else if (!changePasswordParams
        .getNewPassword()
        .equals(changePasswordParams.getConfirmationPassword())) {
      throw new RestProcessingException("password_confirmation_not_match");
    }

    user.setPassword(passwordHash(changePasswordParams.getNewPassword()));
    user.setLastPasswordChangeTime(new Date());
    database.update(user);
  }

  @RequestMapping(
      value = "updateOwnPassword",
      method = PUT,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void updateOwnPassword(@RequestBody IChangePasswordParams changePasswordParams) {
    if (!sessionState
        .getUser()
        .getPassword()
        .equals(passwordHash(changePasswordParams.getActualPassword()))) {
      throw new RestProcessingException("wrong_password");
    }

    changePasswordParams.setId(sessionState.getUser().getId());
    updatePassword(changePasswordParams);
  }

  @RequestMapping(
      value = "blockUser",
      method = PUT,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void blockUser(@RequestBody IBlockUserParams blockParams) {
    User user = database.findById(User.class, blockParams.getId());
    user.setBlocked(blockParams.getBlock());

    database.update(user);
  }

  @RequestMapping(
      method = PUT,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void update(@RequestBody User entity) {
    User user = database.findById(User.class, entity.getId());

    // update does not change password!
    entity.setPassword(user.getPassword());
    database.update(entity.getPerson());
    database.update(entity);
  }
}
