package pl.matsuo.core.service.report;

import org.springframework.stereotype.Service;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.model.query.QueryBuilder;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.report.IPrintsReportParams;

import static pl.matsuo.core.model.query.QueryBuilder.*;


@Service
public class PrintsReportService extends AbstractReportService<IPrintsReportParams> {


  /**
   * Buduje zapytanie do bazy na podstawie przekazanych parametrow
   * @param params parametry przekazane w requescie
   * @return Zwraca obiekt zapytania
   */
  private static Query<KeyValuePrint> buildQuery(IPrintsReportParams params) {
    return QueryBuilder.query(KeyValuePrint.class, new Condition[] {
        maybe(params.getStartDate(), ge("createdTime", params.getStartDate())),
        maybe(params.getEndDate(), le("createdTime", params.getEndDate())),
        maybeEq(params.getPatient(), "patient"),
        maybeEq(params.getPayer(), "payer"),
        maybeEq(params.getPrintClass(), "printClass")
    }).initializer(new PrintInitializer());
  }


  @Override
  protected void injectModel(DataModelBuilder dataModel, IPrintsReportParams params) {
    dataModel.put("prints", database.find(buildQuery(params)))
        .maybePut(params.getIdPatient(), "patient", Person.class)
        .maybePut(params.getIdPayer(), "payer", AbstractParty.class);
  }
}

