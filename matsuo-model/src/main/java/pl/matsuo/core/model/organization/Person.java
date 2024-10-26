package pl.matsuo.core.model.organization;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.TemporalType.DATE;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.organization.address.Address;

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

  private String pesel;

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
