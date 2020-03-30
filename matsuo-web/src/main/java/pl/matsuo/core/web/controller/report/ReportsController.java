package pl.matsuo.core.web.controller.report;

import static java.util.Arrays.asList;
import static org.apache.commons.io.IOUtils.write;
import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.matsuo.core.util.collection.CollectionUtil.toMap;

import java.util.Map;
import java.util.function.BiFunction;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.print.IPrintsRendererService;
import pl.matsuo.core.service.report.IReportService;

@RestController
// wyjątkowo ten kontroler jest transakcyjny! ze względu na specyficzny sposób obsługi requestów
@Transactional
@RequestMapping("/reports")
public class ReportsController {

  @Autowired Database database;
  @Autowired IPrintsRendererService printsRendererService;
  @Autowired IFacadeBuilder facadeBuilder;
  Map<String, IReportService> reportServicesMap;

  @RequestMapping(value = "/pdf/{reportName}", method = GET)
  public void generateReportPdf(
      @PathVariable("reportName") String reportName,
      @RequestParam Map<String, String> params,
      HttpServletResponse response) {
    generateReport(
        "pdf",
        "application/pdf",
        printsRendererService::generatePrint,
        reportName,
        params,
        response);
  }

  @RequestMapping(value = "/xls/{reportName}", method = GET)
  public void generateReportXls(
      @PathVariable("reportName") String reportName,
      @RequestParam Map<String, String> params,
      HttpServletResponse response) {
    generateReport(
        "xls",
        "application/vnd.ms-excel",
        printsRendererService::renderHtml,
        reportName,
        params,
        response);
  }

  /** Methoda generująca generycznie raporty. */
  protected <E> void generateReport(
      String outputType,
      String contentType,
      BiFunction<String, Object, byte[]> method,
      String reportName,
      Map<String, String> params,
      HttpServletResponse response) {
    try {
      IReportService<E> reportService = reportServicesMap.get(reportName);

      if (reportService == null) {
        throw new IllegalArgumentException("Unknown report " + reportName);
      }

      Map<String, Object> model =
          reportService.buildModel(
              facadeBuilder.createFacade(
                  params,
                  (Class<E>) resolveTypeArgument(reportService.getClass(), IReportService.class)));
      model.put("outputFormat", outputType);

      String templateName = reportService.getTemplateName() + ".ftl";
      byte[] html = method.apply(templateName, model);
      response.setContentType(contentType);
      response.setContentLength(html.length);
      response.addHeader(
          "Content-Disposition", "attachment; filename=\"" + reportName + "." + outputType + "\"");

      write(html, response.getOutputStream());

    } catch (Exception e) {
      throw new RuntimeException(
          "Exception while generating report " + reportName + " for outputType " + outputType, e);
    }
  }

  @Autowired(required = false)
  public void setReportServices(IReportService[] reportServices) {
    reportServicesMap = toMap(asList(reportServices), "defaultTemplateName");
  }
}
