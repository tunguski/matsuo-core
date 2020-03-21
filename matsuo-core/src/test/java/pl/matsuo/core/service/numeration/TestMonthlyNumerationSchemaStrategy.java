package pl.matsuo.core.service.numeration;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.DateUtil.*;

import java.util.Date;
import org.junit.Test;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;

public class TestMonthlyNumerationSchemaStrategy {

  MonthlyNumerationSchemaStrategy strategy = new MonthlyNumerationSchemaStrategy();

  @Test
  public void testCreateNumeration() throws Exception {
    NumerationSchema numerationSchema = new NumerationSchema();
    numerationSchema.setCode("test_code");
    numerationSchema.setPattern("test_code");

    Date date = date(2014, 7, 18);

    Numeration numeration = strategy.createNumeration(numerationSchema, date);

    assertEquals("test_code", numeration.getCode());
    assertEquals(date(2014, 7, 1), numeration.getStartDate());
    assertEquals(date(2014, 7, 31), numeration.getEndDate());
  }
}
