package pl.matsuo.core.test.data;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.query.QueryBuilder;
import pl.matsuo.core.test.NumerationConfig;

import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


@ContextConfiguration(classes = { NumerationConfig.class, MediqTestData.class, PersonTestData.class })
public class TestPersonTestData extends AbstractDbTest {


    @Test
    public void testDataInDatabase() {
        List<Person> persons = database.find(query(Person.class));
        assertEquals(892, persons.size());
    }
}
