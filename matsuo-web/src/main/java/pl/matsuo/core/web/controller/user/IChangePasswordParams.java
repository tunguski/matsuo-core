package pl.matsuo.core.web.controller.user;

import pl.matsuo.core.params.IRequestParams;

public interface IChangePasswordParams extends IRequestParams {

  Long getId();

  void setId(Long id);

  String getActualPassword();

  void setActualPassword(String actualPassword);

  String getNewPassword();

  void setNewPassword(String newPassword);

  String getConfirmationPassword();

  void setConfirmationPassword(String confirmationPassword);
}
