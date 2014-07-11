package pl.matsuo.core.model.organization;

import com.tunguski.validator.PESEL;
import pl.matsuo.core.model.organization.address.Address;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.TemporalType.*;


@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstName", "secondName", "pesel"}))
public class Person extends AbstractParty {


  private String firstName;
  private String secondName;
  private String lastName;
  @Enumerated(STRING)
  private Sex sex;
  @PESEL
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


  // getters
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getSecondName() {
    return secondName;
  }
  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public Sex getSex() {
    return sex;
  }
  public void setSex(Sex sex) {
    this.sex = sex;
  }
  public String getPesel() {
    return pesel;
  }
  public void setPesel(String pesel) {
    this.pesel = pesel;
  }
  public Date getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
  public String getIdentificationDocument() {
    return identificationDocument;
  }
  public void setIdentificationDocument(String identificationDocument) {
    this.identificationDocument = identificationDocument;
  }
  public String getIdentificationDocumentNumber() {
    return identificationDocumentNumber;
  }
  public void setIdentificationDocumentNumber(String identificationDocumentNumber) {
    this.identificationDocumentNumber = identificationDocumentNumber;
  }
  public Boolean getNoIdentificationDocument() {
    return noIdentificationDocument;
  }
  public void setNoIdentificationDocument(Boolean noIdentificationDocument) {
    this.noIdentificationDocument = noIdentificationDocument;
  }
  public String getDisabilities() {
    return disabilities;
  }
  public void setDisabilities(String disabilities) {
    this.disabilities = disabilities;
  }
  public Address getCorrespondenceAddress() {
    return correspondenceAddress;
  }
  public void setCorrespondenceAddress(Address correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }
}

