package pl.matsuo.core.web.controller.report;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.matsuo.core.model.report.IPrintsReportParams;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.print.IPrintsRendererService;
import pl.matsuo.core.service.print.PrintsRendererService;
import pl.matsuo.core.service.report.AbstractReportService;
import pl.matsuo.core.service.report.DataModelBuilder;
import pl.matsuo.core.service.report.IReportService;
import pl.matsuo.core.test.data.TestSessionState;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static pl.matsuo.core.util.ReflectUtil.*;

/**
 * Created by marek on 11.04.14.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestReportsController {


  @InjectMocks
  protected ReportsController reportsController = new ReportsController();
  @Spy
  IPrintsRendererService printsRendererService = new PrintsRendererService() {
    @Override
    public byte[] renderHtml(String templateName, Object dataModel) {
      return "Rendered Html OK".getBytes();
    }

    @Override
    public byte[] generatePrint(String templateName, Object dataModel) {
      return "Rendered Print OK".getBytes();
    }
  };
  @Mock
  Database database;
  @Mock
  TestSessionState sessionState;
  @Spy
  FacadeBuilder facadeBuilder = new FacadeBuilder();


  @Before
  public void setUp() {
    TestReportService testReportService = new TestReportService();
    reportsController.setReportServices(new IReportService[] { testReportService });

    when(printsRendererService.renderHtml(anyString(), anyObject())).thenReturn("Rendered OK".getBytes());
  }


  @Test
  public void testSetReportServices() {
    Map<String, IReportService> reportServicesMap = getValue(reportsController, "reportServicesMap");

    assertEquals(1, reportServicesMap.size());
    assertNotNull(reportServicesMap.get("testReport"));
  }


  @Test
  public void testGenerateReportPdf() throws Exception {
    MockHttpServletResponse response = new MockHttpServletResponse();
    reportsController.generateReportPdf("testReport", new HashMap<>(), response);
  }

  @Test
  public void testGenerateReportXls() throws Exception {
    MockHttpServletResponse response = new MockHttpServletResponse();
    reportsController.generateReportXls("testReport", new HashMap<>(), response);
  }


  class TestReportService extends AbstractReportService<IPrintsReportParams> {


    public TestReportService() {
      database = TestReportsController.this.database;
      sessionState = TestReportsController.this.sessionState;
    }

    @Override
    protected void injectModel(DataModelBuilder dataModel, IPrintsReportParams params) {
    }
  }
}
