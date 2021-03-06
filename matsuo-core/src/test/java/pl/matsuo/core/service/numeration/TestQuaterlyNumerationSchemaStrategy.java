package pl.matsuo.core.service.numeration;

import static org.junit.Assert.assertEquals;
import static pl.matsuo.core.util.DateUtil.date;

import java.util.Date;
import org.junit.Test;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

public class TestQuaterlyNumerationSchemaStrategy {

  QuaterlyNumerationSchemaStrategy strategy = new QuaterlyNumerationSchemaStrategy();

  @Test
  public void testCreateNumeration() {
    NumerationSchema numerationSchema = new NumerationSchema();
    numerationSchema.setCode("test_code");

    Date date = date(2014, 7, 18);

    Numeration numeration = strategy.createNumeration(numerationSchema, date);

    assertEquals("test_code", numeration.getCode());
    assertEquals(date(2014, 6, 1), numeration.getStartDate());
    assertEquals(date(2014, 8, 30), numeration.getEndDate());
  }
}
