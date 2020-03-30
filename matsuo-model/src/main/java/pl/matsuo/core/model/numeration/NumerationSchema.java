package pl.matsuo.core.model.numeration;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Getter
@Setter
public class NumerationSchema extends AbstractEntity {

  protected Integer value;
  @NotNull protected Integer minValue;
  protected Integer maxValue;
  protected String code;
  protected String pattern;
  /** How not numeration instances should be created basing on data contained in this schema. */
  protected String creationStrategy;
}
