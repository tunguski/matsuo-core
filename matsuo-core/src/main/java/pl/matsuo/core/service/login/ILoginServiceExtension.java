package pl.matsuo.core.service.login;

import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.user.User;

public interface ILoginServiceExtension {

  void createAccount(OrganizationUnit organizationUnit, User user);
}
