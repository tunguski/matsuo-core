package pl.matsuo.core.test.data;

import org.springframework.stereotype.Component;
import pl.matsuo.core.model.organization.OrganizationUnit;

@Component
public class MediqTestData extends AbstractTestData {

  private Integer idMediq;

  public static final String MEDIQ = "MEDIQ";

  @Override
  public void execute() {
    OrganizationUnit mediq =
        createCompany(
            "Mediq sp. z o.o.", MEDIQ, "Legionowo", "Piłsudskiego 20", "5361188849", "010313501");
    idMediq = mediq.getId();
  }

  private OrganizationUnit createCompany(
      String fullName, String code, String town, String street, String nip, String regon) {
    OrganizationUnit organizationUnit = new OrganizationUnit();

    organizationUnit.setFullName(fullName);
    organizationUnit.setCode(code);
    organizationUnit.getAddress().setTown(town);
    organizationUnit.getAddress().setStreet(street);
    organizationUnit.setNip(nip);
    organizationUnit.setRegon(regon);
    database.create(organizationUnit);

    organizationUnit.setIdBucket(organizationUnit.getId());
    organizationUnit.getAddress().setIdBucket(organizationUnit.getId());

    database.update(organizationUnit);

    return organizationUnit;
  }

  public void withIdBucket(Runnable runnable) {
    withIdBucket(idMediq, runnable);
  }
}
