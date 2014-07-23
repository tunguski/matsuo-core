package pl.matsuo.core.service.numeration;

import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

import java.util.Date;


/**
 * Created by marek on 23.07.14.
 */
public interface NumerationSchemaStrategy {


  /**
   * Creates new numeration basing on numerationSchema instance and date which must be actual in numeration instance.
   */
  Numeration createNumeration(NumerationSchema numerationSchema, Date date);
}

