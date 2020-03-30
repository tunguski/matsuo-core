package pl.matsuo.core.service.login;

public interface ILoginService {

  String login(LoginData loginData);

  void activateAccount(String ticket);

  void remindPassword(String username);

  String createAccount(CreateAccountData createAccountData, boolean sendMail);
}
