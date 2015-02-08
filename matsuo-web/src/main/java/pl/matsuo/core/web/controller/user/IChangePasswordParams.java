package pl.matsuo.core.web.controller.user;


import pl.matsuo.core.params.IRequestParams;


/**
 * Created by marek on 18.05.14.
 */
public interface IChangePasswordParams extends IRequestParams {


  Integer getId();
  void setId(Integer id);
  String getActualPassword();
  void setActualPassword(String actualPassword);
  String getNewPassword();
  void setNewPassword(String newPassword);
  String getConfirmationPassword();
  void setConfirmationPassword(String confirmationPassword);
}

