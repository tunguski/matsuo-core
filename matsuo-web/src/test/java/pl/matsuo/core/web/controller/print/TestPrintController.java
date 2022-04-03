package pl.matsuo.core.web.controller.print;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.conf.GeneralConfig;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.report.IPrintsReportParams;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.print.AbstractPrintService;
import pl.matsuo.core.service.print.PrintsRendererService;
import pl.matsuo.core.service.template.TemplateRegistryImpl;

@ContextConfiguration(
    classes = {
      PrintController.class,
      PrintsRendererService.class,
      GeneralConfig.class,
      TestPrintController.TemplateNamePrintService.class,
      TemplateRegistryImpl.class,
      SampleTemplate.class
    })
public class TestPrintController extends AbstractDbTest {

  @Autowired PrintController printController;
  @Autowired FacadeBuilder facadeBuilder;

  KeyValuePrint print;

  @Before
  public void setupDatabase() {
    print = new KeyValuePrint();
    print.setPrintClass(SampleTemplate.class);

    database.create(print);
  }

  @Test
  public void testGeneratePrint() {
    MockHttpServletResponse response = new MockHttpServletResponse();
    printController.generatePrint(print.getId(), response);
  }

  @Test
  public void testGeneratePrint1() {
    MockHttpServletResponse response = new MockHttpServletResponse();
    printController.generatePrint(print, response);
  }

  @Test
  public void testGeneratePrint2() {
    MockHttpServletResponse response = new MockHttpServletResponse();
    Map<String, Object> dataModel = new HashMap<>();
    printController.generatePrint("sampleTemplate", "fileName", dataModel, response);
  }

  @Test
  public void testFindPrints() {
    printController.findPrints(
        facadeBuilder.createFacade(new HashMap<>(), IPrintsReportParams.class),
        "keyValuePrint.idEntity");
  }

  @Test
  public void testList() {
    printController.list(facadeBuilder.createFacade(new HashMap<>(), IPrintsReportParams.class));
  }

  @Test
  public void testListByIdEntities() {
    printController.listByIdEntities(asList(17L));
  }

  public static interface SampleTemplate extends IPrintFacade {}

  @Service
  public static class TemplateNamePrintService extends AbstractPrintService<SampleTemplate> {

    @Override
    protected void buildModel(SampleTemplate print, Map dataModel) {}

    @Override
    public String getFileName(SampleTemplate print) {
      return "templateName";
    }
  }
}
