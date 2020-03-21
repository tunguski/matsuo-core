package pl.matsuo.core.service.numeration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.util.DateUtil.*;

import java.util.Date;
import org.junit.Test;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.service.db.Database;

public class TestNumerationServiceImpl {

  @Test
  public void testGetNumber() {
    NumerationServiceImpl numerationService = new NumerationServiceImpl();

    Database database = mock(Database.class);

    Numeration numeration = new Numeration();
    numeration.setPattern("FV/${numerationYear}/${numerationMonth}/${value}");
    // such a big number to check proper formating
    numeration.setValue(254350);
    numeration.setStartDate(date(2014, 7, 1));

    when(database.findOne(any(Query.class))).thenReturn(numeration);

    numerationService.database = database;

    assertEquals("FV/2014/8/254350", numerationService.getNumber("INVOICE", new Date(), true));
  }
}
