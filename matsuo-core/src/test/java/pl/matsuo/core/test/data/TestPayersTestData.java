package pl.matsuo.core.test.data;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.query.QueryBuilder;
import pl.matsuo.core.test.NumerationConfig;

import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;

@ContextConfiguration(classes = { NumerationConfig.class, MediqTestData.class, PayersTestData.class })
public class TestPayersTestData extends AbstractDbTest {


  @Test
  public void testDataInDatabase() {
    List<OrganizationUnit> organizationUnits = database.find(query(OrganizationUnit.class));
    assertEquals(15, organizationUnits.size());
  }
}