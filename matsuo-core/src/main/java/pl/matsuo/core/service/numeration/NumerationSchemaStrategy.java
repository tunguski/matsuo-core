package pl.matsuo.core.service.numeration;

import java.util.Date;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

public interface NumerationSchemaStrategy {

  /**
   * Creates new numeration basing on numerationSchema instance and date which must be actual in
   * numeration instance.
   */
  Numeration createNumeration(NumerationSchema numerationSchema, Date date);
}
