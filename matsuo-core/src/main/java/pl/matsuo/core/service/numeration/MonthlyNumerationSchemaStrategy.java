package pl.matsuo.core.service.numeration;

import static java.util.Calendar.*;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;
import pl.matsuo.core.util.DateUtil;

/** Created by marek on 23.07.14. */
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

    Calendar cal = DateUtil.cal(date, 0, 0);
    cal.set(DATE, 1);

    numeration.setStartDate(cal.getTime());

    cal.set(DATE, cal.getActualMaximum(DATE));

    numeration.setEndDate(cal.getTime());

    return numeration;
  }
}
