package pl.matsuo.core.model.execution;

import jakarta.persistence.Entity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

/**
 * Model bazodanowy dla zapisów o wykonaniach serwisów.
 *
 * @since 11-07-2013
 */
@Entity
@Getter
@Setter
public class Execution extends AbstractEntity {

  private Date runDate;
  private Boolean success;
  private String beanName;
}
