package pl.matsuo.core.service.numeration;

import org.junit.Test;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.service.db.Database;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by tunguski on 15.09.13.
 */
public class TestNumerationServiceImpl {


  @Test
  public void testGetNumber() {
    NumerationServiceImpl numerationService = new NumerationServiceImpl();

    Database database = mock(Database.class);

    Numeration numeration = new Numeration();
    numeration.setPattern("TEST/$");
    numeration.setValue(20);

    when(database.findOne(any(Query.class))).thenReturn(numeration);

    numerationService.database = database;

    assertEquals("TEST/20", numerationService.getNumber("INVOICE"));
  }
}

