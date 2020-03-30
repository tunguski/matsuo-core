package pl.matsuo.core.service.report;

import static pl.matsuo.core.util.DateUtil.date;
import static pl.matsuo.core.util.DateUtil.isoFormat;

import java.util.HashMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.model.report.IPrintsReportParams;
import pl.matsuo.core.test.AbstractReportTest;

@ContextConfiguration(classes = {PrintsReportService.class})
public class TestPrintsReport extends AbstractReportTest<IPrintsReportParams> {

  @Autowired PrintsReportService printsReportService;

  // FIXME: create test data and restore test
  //  @Test
  //  public void full() throws Exception {
  //    HashMap<String,Object> params = new HashMap<>();
  //
  //    testCreatePDF(facadeBuilder.createFacade(params, IPrintsReportParams.class));
  //  }

  @Test
  public void empty() throws Exception {
    HashMap<String, Object> params = new HashMap<>();
    params.put("startDate", isoFormat(date(3000, 0, 1)));

    // FIXME: move report ftl to matsuo-core
    //    testCreatePDF(facadeBuilder.createFacade(params, IPrintsReportParams.class));
  }

  @Override
  protected String getPrintFileName() {
    return "/print/printsReport.ftl";
  }
}
