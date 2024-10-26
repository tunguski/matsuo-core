package pl.matsuo.core.model.organization;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

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
  private String regon;

  /** Parent organization unit. If null, entity describes standalone organization, like company. */
  @ManyToOne private OrganizationUnit parentOrganizationUnit;

  @Override
  @Transient
  public String getName() {
    return fullName;
  }
}
