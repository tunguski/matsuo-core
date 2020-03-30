package pl.matsuo.core.test.data;

import static org.junit.Assert.assertEquals;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import java.util.List;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.test.NumerationConfig;

@ContextConfiguration(classes = {NumerationConfig.class, MediqTestData.class, PersonTestData.class})
public class TestPersonTestData extends AbstractDbTest {

  @Test
  public void testDataInDatabase() {
    List<Person> persons = database.find(query(Person.class));
    assertEquals(892, persons.size());
  }
}
