package pl.matsuo.core.web.controller.login;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.permission.IPermissionService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.web.controller.exception.RestProcessingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.SecurityUtil.*;
import static pl.matsuo.core.util.StringUtil.*;


/**
 * Created by tunguski on 22.12.13.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


  @Autowired
  SessionState sessionState;
  @Autowired
  Database database;
  @Autowired
  IPermissionService permissionService;


  @RequestMapping(method = GET)
  public String loggedUsername() {
    if (sessionState.getUser() == null) {
      return null;
    } else {
      return sessionState.getUser().getUsername();
    }
  }


  @RequestMapping(method = POST)
  public String login(@RequestBody LoginData loginData) {
    if (!notEmpty(loginData.getUsername()) || !notEmpty(loginData.getPassword())) {
      throw new UnauthorizedException();
    }

    User user = database.findOne(query(User.class, eq("username", loginData.getUsername()))
        .initializer(new UserInitializer()));
    if (user != null && !user.getBlocked()
        && user.getPassword().equals(passwordHash(loginData.getPassword()))) {
      user.setLastLoginTime(new Date());
      user.getGroups().size();
      database.update(user);

      sessionState.setUser(user);

      return loginData.getUsername();
    } else {
      // sleep on failure to obfuscate calculation length and slow down next login attempt
      try {
        Thread.sleep(1000 + (long) (Math.random() * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      throw new UnauthorizedException();
    }
  }


  @RequestMapping(value = "/activateAccount/{ticket}")
  public void activateAccount(@PathVariable("ticket") String ticket) {
    List<User> users = database.find(query(User.class, eq("unblockTicket", ticket)));

    if (users.size() != 1) {
      throw new RestProcessingException("Unable to identify account");
    }

    User user = users.get(0);

    if (!user.getBlocked()) {
      throw new RestProcessingException("Account is not blocked");
    }

    user.setBlocked(false);
    user.setUnblockTicket(null);
    database.update(user);
  }


  @RequestMapping(value = "/createAccount", method = POST)
  public String createAccount(@RequestBody CreateAccountData createAccountData) {
    User user = database.findOne(query(User.class, eq("username", createAccountData.getUsername())));
    if (user != null) {
      throw new RestProcessingException("Username already used");
    }

    OrganizationUnit organizationUnit = new OrganizationUnit();
    organizationUnit.setFullName(createAccountData.getCompanyName());
    organizationUnit.setShortName(createAccountData.getCompanyShortName());
    organizationUnit.setNip(createAccountData.getCompanyNip());
    database.create(organizationUnit);
    Assert.notNull(organizationUnit.getId());
    // update idBucket for newly created organization
    organizationUnit.setIdBucket(organizationUnit.getId());
    database.update(organizationUnit);

    sessionState.setIdBucket(organizationUnit.getId());

    Person person = new Person();
    database.create(person);

    user = new User();
    user.setPerson(person);
    user.setUsername(createAccountData.getUsername());
    user.setPassword(passwordHash(createAccountData.getPassword()));
    // only a little bit randomness
    user.setUnblockTicket(RandomStringUtils.randomAlphabetic(16));
    user.setBlocked(true);
    user.setIdBucket(organizationUnit.getId());
    database.create(user);

    // fixme: send this by email to username
    return "Account confirmation ticket: " + user.getUnblockTicket();
  }


  @RequestMapping(value = "/logoff", method = POST)
  @ResponseStatus(NO_CONTENT)
  public void logoff() {
    permissionService.logoff();
  }


  @RequestMapping(value = "/remindPassword/{username}", method = POST)
  @ResponseStatus(NO_CONTENT)
  public void remindPassword(@PathVariable("username") String username) {
    username.length();
  }


  @RequestMapping(value = "/user", method = GET)
  public User loggedUser() {
    return sessionState.getUser();
  }


  @RequestMapping(value = "/permissions", method = POST)
  public List<Boolean> permissions(@RequestBody List<String> permissions) {
    List<Boolean> permissionsCheck = new ArrayList<>();

    for (String permission : permissions) {
      permissionsCheck.add(permissionService.isPermitted(permission));
    }


    return permissionsCheck;
  }


  @RequestMapping(value = "/loginTime", method = GET)
  public Long loginTime() {
    return sessionState.getLoginTime();
  }


  @ResponseStatus(UNAUTHORIZED)
  public static class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }
}

