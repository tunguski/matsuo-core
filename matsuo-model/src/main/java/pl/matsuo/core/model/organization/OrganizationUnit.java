package pl.matsuo.core.model.organization;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pl.matsuo.validator.REGON;

/**
 * Organization unit - describes company, division etc.
 *
 * @since Jul 23, 2013
 */
@Entity
@Getter
@Setter
public class OrganizationUnit extends AbstractParty {

  @NotEmpty private String fullName;
  private String shortName;
  private String code;
  @ManyToMany private Set<Person> employees = new HashSet<>();
  @REGON private String regon;
  /** Parent organization unit. If null, entity describes standalone organization, like company. */
  @ManyToOne private OrganizationUnit parentOrganizationUnit;

  @Override
  @Transient
  public String getName() {
    return fullName;
  }
}
