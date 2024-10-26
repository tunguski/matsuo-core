package pl.matsuo.core.model.message;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.validation.EntityReference;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractMessage extends AbstractEntity {

  @EntityReference(value = AbstractParty.class)
  // FIXME: przy multiMessage brak ustawienia
  // @NotNull
  private Long idParty;

  @NotEmpty
  @Column(columnDefinition = "clob")
  private String text;
}
