package pl.matsuo.core.model.organization;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.address.Address;

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

  private String nip;

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
