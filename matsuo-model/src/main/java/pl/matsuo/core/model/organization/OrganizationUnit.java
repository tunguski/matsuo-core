package pl.matsuo.core.model.organization;

import com.tunguski.validator.REGON;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;


/**
 * Organization unit - describes company, division etc.
 *
 * @author Marek Romanowski
 * @since Jul 23, 2013
 */
@Entity
public class OrganizationUnit extends AbstractParty {


  @NotEmpty
  private String fullName;
  private String shortName;
  private String code;
  @ManyToMany
  private Set<Person> employees = new HashSet<>();
  @REGON
  private String regon;
  /**
   * Parent organization unit. If null, entity describes standalone organization, like company.
   */
  @ManyToOne
  private OrganizationUnit parentOrganizationUnit;


  @Override
  @Transient
  public String getName() {
    return fullName;
  }


  // getters
  public String getFullName() {
    return fullName;
  }
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  public String getShortName() {
    return shortName;
  }
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public Set<Person> getEmployees() {
    return employees;
  }
  public void setEmployees(Set<Person> employees) {
    this.employees = employees;
  }
  public String getRegon() {
    return regon;
  }
  public void setRegon(String regon) {
    this.regon = regon;
  }
  public OrganizationUnit getParentOrganizationUnit() {
    return parentOrganizationUnit;
  }
  public void setParentOrganizationUnit(OrganizationUnit parentOrganizationUnit) {
    this.parentOrganizationUnit = parentOrganizationUnit;
  }
}

