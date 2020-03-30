package pl.matsuo.core.model.numeration;

import static javax.persistence.TemporalType.*;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.TemporalEntity;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"idBucket", "code", "startDate"})})
@Getter
@Setter
public class Numeration extends AbstractEntity implements TemporalEntity {

  @NotNull protected Integer value;
  /** Owning entity id. May be null if this numeration is 'general' (depending on the context). */
  protected Integer idEntity;

  @NotNull protected Integer minValue;
  protected Integer maxValue;
  @NotNull protected String code;
  @NotNull protected String pattern;

  @Temporal(DATE)
  private Date startDate;

  @Temporal(DATE)
  private Date endDate;
}
