package pl.matsuo.core.model;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityHistory extends AbstractEntity {

  @NotNull protected Long idEntity;
}
