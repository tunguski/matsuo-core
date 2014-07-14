package pl.matsuo.core.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tunguski.validator.NIP;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.address.Address;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.*;
import static javax.persistence.InheritanceType.*;


/**
 * Nadklasa dla wszystkich obiektów występujących jako podmioty w dowolnym kontekście. Podstawowe
 * przypadki to firma i osoba prywatna.
 * @author Marek Romanowski
 * @since Jul 23, 2013
 */
@Entity
@Inheritance(strategy=SINGLE_TABLE)
public abstract class AbstractParty extends AbstractEntity {


  @NIP
  private String nip;
  @NotNull @Valid
  @OneToOne(cascade = ALL)
  private Address address = new Address();


  @Transient @JsonIgnore
  public abstract String getName();


  // getters
  public String getNip() {
    return nip;
  }
  public void setNip(String nip) {
    this.nip = nip != null ? nip.replace("-", "") : null;
  }
  public Address getAddress() {
    return address;
  }
  public void setAddress(Address address) {
    this.address = address;
  }
}

