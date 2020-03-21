package pl.matsuo.core.model.organization.initializer;

import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.organization.OrganizationUnit;

public class CompanyInitializer implements Initializer<OrganizationUnit> {

  @Override
  public void init(OrganizationUnit element) {
    element.getEmployees().size();
  }
}
