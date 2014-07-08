package pl.matsuo.core.model.organization.address;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.EnumType.*;


@Entity
public class Address extends AbstractEntity {

  private String town;
  private String street;
  private String houseNumber;
  private String apartmentNumber;
  @Pattern(regexp = "[0-9]{2}(-)?[0-9]{3}")
  private String zipCode;
  @Enumerated(STRING)
  private CountryType country;
  private String phone;
  private String email;
  private String gmina;
  private String comment;


  // getters
  public String getTown() {
    return town;
  }
  public void setTown(String town) {
    this.town = town;
  }
  public String getStreet() {
    return street;
  }
  public void setStreet(String street) {
    this.street = street;
  }
  public String getHouseNumber() {
    return houseNumber;
  }
  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }
  public String getApartmentNumber() {
    return apartmentNumber;
  }
  public void setApartmentNumber(String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }
  public String getZipCode() {
    return zipCode;
  }
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
  public CountryType getCountry() {
    return country;
  }
  public void setCountry(CountryType country) {
    this.country = country;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getGmina() {
    return gmina;
  }
  public void setGmina(String gmina) {
    this.gmina = gmina;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }
}
