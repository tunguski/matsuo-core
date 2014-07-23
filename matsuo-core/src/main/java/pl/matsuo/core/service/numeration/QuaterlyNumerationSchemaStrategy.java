package pl.matsuo.core.service.numeration;

import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

import java.util.Date;

import static pl.matsuo.core.util.DateUtil.*;


/**
 * Created by marek on 23.07.14.
 */
@Service
public class QuaterlyNumerationSchemaStrategy implements NumerationSchemaStrategy {


  @Override
  public Numeration createNumeration(NumerationSchema numerationSchema, Date date) {
    Numeration numeration = new Numeration();
    numeration.setCode(numerationSchema.getCode());
    numeration.setValue(numerationSchema.getMinValue());
    numeration.setMinValue(numerationSchema.getMinValue());
    numeration.setMaxValue(numerationSchema.getMaxValue());
    numeration.setPattern(numerationSchema.getPattern());

    numeration.setStartDate(getQuaterStart(date));
    numeration.setEndDate(getQuaterEnd(date));

    return numeration;
  }
}

