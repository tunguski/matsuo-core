package pl.matsuo.core.web.controller.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.login.CreateAccountData;
import pl.matsuo.core.service.login.ILoginService;
import pl.matsuo.core.service.login.LoginData;
import pl.matsuo.core.service.permission.IPermissionService;
import pl.matsuo.core.service.session.SessionState;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


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
  ILoginService loginService;
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
    return loginService.login(loginData);
  }


  @RequestMapping(value = "/activateAccount/{ticket}")
  public void activateAccount(@PathVariable("ticket") String ticket) {
    loginService.activateAccount(ticket);
  }


  @RequestMapping(value = "/createAccount", method = POST)
  public String createAccount(@RequestBody CreateAccountData createAccountData) {
    return "Account confirmation ticket: " + loginService.createAccount(createAccountData);
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
}

