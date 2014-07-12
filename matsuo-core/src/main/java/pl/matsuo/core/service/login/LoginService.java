package pl.matsuo.core.service.login;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.exception.UnauthorizedException;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.GroupEnum;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.session.SessionState;

import java.util.Date;
import java.util.List;

import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.SecurityUtil.*;
import static pl.matsuo.core.util.StringUtil.*;


/**
 * Created by marek on 12.07.14.
 */
@Service
public class LoginService implements ILoginService {


  @Autowired
  SessionState sessionState;
  @Autowired
  Database database;
  @Autowired(required = false)
  ILoginServiceExtension[] extensions;


  @Override
  public String login(LoginData loginData) {
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


  @Override
  public void activateAccount(String ticket) {
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


  @Override
  public String createAccount(CreateAccountData createAccountData) {
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
    List<Group> groups = database.findAsAdmin(query(Group.class, eq("name", GroupEnum.SUPERVISOR.name())));
    Assert.isTrue(groups.size() == 1);
    user.getGroups().add(groups.get(0));
    user.setUsername(createAccountData.getUsername());
    user.setPassword(passwordHash(createAccountData.getPassword()));
    // only a little bit randomness
    user.setUnblockTicket(RandomStringUtils.randomAlphabetic(16));
    user.setBlocked(true);
    user.setIdBucket(organizationUnit.getId());
    database.create(user);

    if (extensions != null) {
      for (ILoginServiceExtension extension : extensions) {
        extension.createAccount(organizationUnit, user);
      }
    }

    // fixme: send this by email to username
    return user.getUnblockTicket();
  }
}

