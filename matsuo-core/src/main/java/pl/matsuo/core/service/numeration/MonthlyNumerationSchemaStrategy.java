package pl.matsuo.core.service.numeration;

import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;


/**
 * Created by marek on 23.07.14.
 */
@Service
public class MonthlyNumerationSchemaStrategy implements NumerationSchemaStrategy {


  @Override
  public Numeration createNumeration(NumerationSchema numerationSchema, Date date) {
    Numeration numeration = new Numeration();
    numeration.setCode(numerationSchema.getCode());
    numeration.setValue(numerationSchema.getMinValue());
    numeration.setMinValue(numerationSchema.getMinValue());
    numeration.setMaxValue(numerationSchema.getMaxValue());
    numeration.setPattern(numerationSchema.getPattern());

    Calendar cal = getInstance();
    cal.setTime(date);
    cal.set(DATE, 1);

    numeration.setStartDate(cal.getTime());

    cal.set(DATE, cal.getActualMaximum(DATE));

    numeration.setEndDate(cal.getTime());

    return numeration;
  }
}

