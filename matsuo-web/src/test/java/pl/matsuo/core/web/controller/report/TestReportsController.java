package pl.matsuo.core.web.controller.report;

import org.junit.Test;
import pl.matsuo.core.service.report.AbstractReportService;
import pl.matsuo.core.service.report.DataModelBuilder;
import pl.matsuo.core.service.report.IReportService;

import java.util.Map;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.ReflectUtil.*;

/**
 * Created by marek on 11.04.14.
 */
public class TestReportsController {


  class TestReportService extends AbstractReportService {
    @Override
    protected void injectModel(DataModelBuilder dataModel, Object params) {
    }
  }


  protected ReportsController reportsController = new ReportsController();


  @Test
  public void testSetReportServices() {
    reportsController.setReportServices(new IReportService[] { new TestReportService() });

    Map<String, IReportService> reportServicesMap = getValue(reportsController, "reportServicesMap");

    assertEquals(1, reportServicesMap.size());
    assertNotNull(reportServicesMap.get("testReport"));
  }
}
