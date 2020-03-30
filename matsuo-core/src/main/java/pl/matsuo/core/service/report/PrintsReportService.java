package pl.matsuo.core.service.report;

import static pl.matsuo.core.model.query.QueryBuilder.ge;
import static pl.matsuo.core.model.query.QueryBuilder.le;
import static pl.matsuo.core.model.query.QueryBuilder.maybe;
import static pl.matsuo.core.model.query.QueryBuilder.maybeEq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import org.springframework.stereotype.Service;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.report.IPrintsReportParams;

@Service
public class PrintsReportService extends AbstractReportService<IPrintsReportParams> {

  /**
   * Buduje zapytanie do bazy na podstawie przekazanych parametrow
   *
   * @param params parametry przekazane w requescie
   * @return Zwraca obiekt zapytania
   */
  private static Query<KeyValuePrint> buildQuery(IPrintsReportParams params) {
    return query(
            KeyValuePrint.class,
            new Condition[] {
              maybe(
                  params.getStartDate(), ge(KeyValuePrint::getCreatedTime, params.getStartDate())),
              maybe(params.getEndDate(), le(KeyValuePrint::getCreatedTime, params.getEndDate())),
              // FIXME: how to reference this?
              //        maybeEq(params.getPatient(), KeyValuePrint::patient),
              //        maybeEq(params.getPayer(), KeyValuePrint::payer),
              maybeEq(params.getPrintClass(), KeyValuePrint::getPrintClass)
            })
        .initializer(new PrintInitializer());
  }

  @Override
  protected void injectModel(DataModelBuilder dataModel, IPrintsReportParams params) {
    dataModel
        .put("prints", database.find(buildQuery(params)))
        .maybePut(params.getIdPatient(), "patient", Person.class)
        .maybePut(params.getIdPayer(), "payer", AbstractParty.class);
  }
}
