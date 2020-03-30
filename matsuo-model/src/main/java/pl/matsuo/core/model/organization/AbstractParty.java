package pl.matsuo.core.model.organization;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.validator.NIP;

/**
 * Nadklasa dla wszystkich obiektów występujących jako podmioty w dowolnym kontekście. Podstawowe
 * przypadki to firma i osoba prywatna.
 *
 * @since Jul 23, 2013
 */
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Getter
@Setter
public abstract class AbstractParty extends AbstractEntity {

  @NIP private String nip;

  @NotNull
  @Valid
  @OneToOne(cascade = ALL)
  private Address address = new Address();

  @Transient
  @JsonIgnore
  public abstract String getName();

  public void setNip(String nip) {
    this.nip = nip != null ? nip.replace("-", "") : null;
  }
}
