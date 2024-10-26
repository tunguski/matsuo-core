package pl.matsuo.core.service.login;

import static java.util.Optional.ofNullable;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.SecurityUtil.passwordHash;
import static pl.matsuo.core.util.StringUtil.notEmpty;
import static pl.matsuo.core.util.function.FunctionalUtil.runtimeEx;

import jakarta.mail.internet.InternetAddress;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import pl.matsuo.core.service.login.model.LoginEmailData;
import pl.matsuo.core.service.mail.IMailService;
import pl.matsuo.core.service.report.DataModelBuilder;
import pl.matsuo.core.service.session.SessionState;

@Service
public class LoginService implements ILoginService {

  @Autowired SessionState sessionState;
  @Autowired Database database;

  @Autowired(required = false)
  ILoginServiceExtension[] extensions;

  @Autowired IMailService mailService;

  @Value("${mail.general.account}")
  String generalMailAccount;

  @Value("${app.name}")
  String appName;

  @Override
  public String login(LoginData loginData) {
    if (!notEmpty(loginData.getUsername()) || !notEmpty(loginData.getPassword())) {
      throw new UnauthorizedException();
    }

    User user =
        database.findOne(
            query(User.class, eq(User::getUsername, loginData.getUsername()))
                .initializer(new UserInitializer()));
    if (user != null
        && !user.getBlocked()
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
    List<User> users = database.find(query(User.class, eq(User::getUnblockTicket, ticket)));

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
  public void remindPassword(String username) {
    ofNullable(database.findOne(query(User.class, eq(User::getUsername, username))))
        .map(
            user ->
                runtimeEx(
                    () ->
                        mailService.sendMail(
                            new InternetAddress(generalMailAccount, appName),
                            new InternetAddress(user.getUsername()),
                            "Przypomnienie hasła",
                            "remindPassword",
                            new DataModelBuilder().put("user", user).getDataModel())));
  }

  @Override
  public String createAccount(CreateAccountData createAccountData, boolean sendMail) {
    if (database.findOne(query(User.class, eq(User::getUsername, createAccountData.getUsername())))
        != null) {
      throw new RestProcessingException("Username already used");
    }

    OrganizationUnit organizationUnit = new OrganizationUnit();
    organizationUnit.setFullName(createAccountData.getCompanyName());
    organizationUnit.setShortName(createAccountData.getCompanyShortName());
    organizationUnit.setNip(createAccountData.getCompanyNip());
    database.create(organizationUnit);
    Assert.notNull(organizationUnit.getId(), "Failed to create organizationUnit");
    // update idBucket for newly created organization
    organizationUnit.setIdBucket(organizationUnit.getId());
    database.update(organizationUnit);

    sessionState.setIdBucket(organizationUnit.getId());

    Person person = new Person();
    person.setFirstName(createAccountData.getUsername());
    database.create(person);

    User user = new User();
    user.setPerson(person);
    List<Group> groups =
        database.findAsAdmin(query(Group.class, eq(Group::getName, GroupEnum.SUPERVISOR.name())));
    Assert.isTrue(groups.size() == 1, "Incorrect number of groups: " + groups.size());
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

    if (sendMail) {
      runtimeEx(
          () ->
              mailService.sendMail(
                  new InternetAddress(generalMailAccount, appName),
                  new InternetAddress(user.getUsername()),
                  "Witamy! Prosimy o weryfikację adresu e-mail",
                  "createAccount",
                  new LoginEmailData(user)));
    }

    return user.getUnblockTicket();
  }
}
