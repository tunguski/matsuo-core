package pl.matsuo.core.model.organization;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.validator.PESEL;

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstName", "secondName", "pesel"}))
@Getter
@Setter
public class Person extends AbstractParty {

  private String firstName;
  private String secondName;
  private String lastName;

  @Enumerated(STRING)
  private Sex sex;

  @PESEL private String pesel;

  @Temporal(DATE)
  private Date birthDate;

  private String identificationDocument;
  private String identificationDocumentNumber;
  private Boolean noIdentificationDocument;
  private String disabilities;

  @OneToOne(cascade = ALL)
  private Address correspondenceAddress;

  @Override
  public String getName() {
    return firstName + " " + lastName;
  }
}
