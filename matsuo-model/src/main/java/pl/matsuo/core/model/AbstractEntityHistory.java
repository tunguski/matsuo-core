package pl.matsuo.core.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityHistory extends AbstractEntity {

  @NotNull protected Long idEntity;
}
