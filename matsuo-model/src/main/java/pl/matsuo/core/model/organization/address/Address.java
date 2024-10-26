package pl.matsuo.core.model.organization.address;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Getter
@Setter
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
}
