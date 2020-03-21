package pl.matsuo.core.service.login;

import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.user.User;

/** Created by marek on 12.07.14. */
public interface ILoginServiceExtension {

  void createAccount(OrganizationUnit organizationUnit, User user);
}
