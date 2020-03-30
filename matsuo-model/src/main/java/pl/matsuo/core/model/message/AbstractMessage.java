package pl.matsuo.core.model.message;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
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
  private Integer idParty;

  @NotEmpty
  @Column(columnDefinition = "clob")
  private String text;
}
