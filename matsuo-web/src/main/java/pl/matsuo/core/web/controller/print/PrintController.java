package pl.matsuo.core.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.model.query.condition.QueryPart;
import pl.matsuo.core.model.report.IPrintsReportParams;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.print.AbstractPrintService;
import pl.matsuo.core.service.print.IPrintsRendererService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.*;
import static javax.servlet.http.HttpServletResponse.*;
import static org.apache.commons.io.IOUtils.*;
import static org.springframework.core.GenericTypeResolver.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.util.StringUtils.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Kontroler generowania druków.
 *
 * @author Marek Romanowski
 * @since Aug 28, 2013
 */
@RestController
@Transactional
@RequestMapping("/prints")
public class PrintController {


  @Autowired
  protected Database database;
  @Autowired
  protected IPrintsRendererService printsRendererService;
  @Autowired
  protected IFacadeBuilder facadeBuilder;
  Map<Class, AbstractPrintService> reportServicesMap;


//  @RequestMapping(value = "/dbGui", method = GET)
//  public void startDbGui() {
//    DatabaseManagerSwing.main(new String[]{ "--url", "jdbc:hsqldb:mem:test", "--user", "sa", "--noexit"});
//  }


  @RequestMapping(value = "/{id}", method = GET)
  public void generatePrint(@PathVariable("id") Integer id, HttpServletResponse response) {
    try {
      KeyValuePrint print = database.findById(KeyValuePrint.class, id, new PrintInitializer());
      if (print == null) {
        response.sendError(SC_NOT_FOUND);
        return;
      }

      generatePrint(print, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Metoda specjalnie nie ma RequestMapping.
   */
  public void generatePrint(KeyValuePrint print, HttpServletResponse response) {
    try {
      AbstractPrintService printService = reportServicesMap.get(print.getPrintClass());

      if (printService == null) {
        throw new IllegalArgumentException("Unknown report " + print.getPrintClass());
      }

      IPrintFacade printFacade = facadeBuilder.createFacade(print, print.getPrintClass());

      Map<String, Object> model = printService.buildModel(printFacade);
      // default dla druków
      model.put("outputFormat", "pdf");

      generatePrint(uncapitalize(print.getPrintClass().getSimpleName()) + ".ftl",
          printService.getFileName(printFacade), model, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  protected List<KeyValuePrint> findPrints(IPrintsReportParams params, String personProperty, QueryPart... queryParts) {
    return database.find(query(KeyValuePrint.class, select("keyValuePrint"),
        maybe(params.getIdPatient(), cond(personProperty + " = " + params.getIdPatient())),
        maybe(params.getIdPayer(), cond("keyValuePrint.fields['buyer.id'] = " + params.getIdPayer())),
        maybe(params.getStartDate(), ge(KeyValuePrint::getCreatedTime, params.getStartDate())),
        maybe(params.getEndDate(), le(KeyValuePrint::getCreatedTime, params.getEndDate())),
        maybeEq(params.getPrintClass(), KeyValuePrint::getPrintClass)
        )
            .parts(queryParts).initializer(new PrintInitializer()));
  }


  @RequestMapping(method = GET)
  public List<KeyValuePrint> list(IPrintsReportParams params) {
    // FIXME! przemyśleć referencję do Appointment - czy to wydzielić do oddzielnego kontrolera, czy coś innego z tym zrobić?
//    List<KeyValuePrint> prints3 = findPrints(params, "appointment.idPatient",
//        new LeftJoinElement("appointment",
//            "pl.matsuo.clinic.model.medical.appointment.Appointment", cond("appointment.id = keyValuePrint.idEntity")));
    // Przypadek KP
    List<KeyValuePrint> prints2 = findPrints(params, "keyValuePrint.fields['buyer.id']"/* , isNull("keyValuePrint.idEntity") */);
    // prints connected directly to somebody
    List<KeyValuePrint> prints = findPrints(params, "keyValuePrint.idEntity");

    Map<Integer, KeyValuePrint> printsMap = new HashMap<>();
    asList(prints, prints2).forEach(collection ->
        collection.forEach(print -> printsMap.put(print.getId(), print)));

    List<KeyValuePrint> result = new ArrayList<>(printsMap.values());
    // reversed order!
    Collections.sort(result, (a, b) -> b.getCreatedTime().compareTo(a.getCreatedTime()));

    if (params.getLimit() != null) {
      result = result.subList(0, params.getLimit());
    }

    return result;
  }


  public void generatePrint(String templateName, String fileName, Object dataModel,
                            HttpServletResponse response) {
    byte[] pdf = printsRendererService.generatePrint(templateName, dataModel);
    response.setContentType("application/pdf");
    response.setContentLength(pdf.length);
    response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

    try {
      write(pdf, response.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Pobiera listę druków dla przekazanych identyfikatorów wizyt.
   */
  @RequestMapping(value = "/list/byIdEntities", method = GET, consumes = {APPLICATION_OCTET_STREAM_VALUE})
  public List<KeyValuePrint> listByIdEntities(@RequestParam("ids") List<Integer> ids) {
    return database.find(query(KeyValuePrint.class, in(KeyValuePrint::getIdEntity, ids)).initializer(new PrintInitializer()));
  }


  @Autowired(required = false)
  public void setReportServices(AbstractPrintService[] reportServices) {
    reportServicesMap = new HashMap<>();
    for (AbstractPrintService reportService : reportServices) {
      reportServicesMap.put(resolveTypeArgument(reportService.getClass(), AbstractPrintService.class), reportService);
    }
  }
}

