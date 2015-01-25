package pl.matsuo.core.service.login;

/**
 * Created by marek on 12.07.14.
 */
public interface ILoginService {

  String login(LoginData loginData);
  void activateAccount(String ticket);
  String createAccount(CreateAccountData createAccountData, boolean sendMail);
}
