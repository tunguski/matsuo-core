package pl.matsuo.core.web.controller.organization;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.FacadeBuilder;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;


/**
 * Created by tunguski on 19.09.13.
 */
public class TestPayerController {


  @Autowired
  Database database = mock(Database.class);
  @Autowired
  PayerController controller = new PayerController();
  FacadeBuilder facadeBuilder = mock(FacadeBuilder.class);


  public TestPayerController() {
    controller.setDatabase(database);
    controller.setFacadeBuilder(facadeBuilder);
  }


  @Test
  public void testLoadingMediq() {
    when(database.find(any(Query.class))).then(invocation -> {
          String queryString = ((AbstractQuery) invocation.getArguments()[0]).printQuery();
          String expected = "FROM pl.matsuo.core.model.organization.AbstractParty abstractParty WHERE " +
              "(lower(firstName) like '%mediq%' OR lower(lastName) like '%mediq%' OR lower(fullName) " +
              "like '%mediq%' OR lower(shortName) like '%mediq%' OR lower(code) like '%mediq%')";
          assertTrue("Query does not match:\n" + queryString.trim() + "\nexpected:\n" + expected,
              queryString.trim().equals(expected));

          return asList(new OrganizationUnit());
    });

    controller.list(queryFacade(IQueryRequestParams.class, "query", "mediq"));
  }
}
