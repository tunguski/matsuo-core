package pl.matsuo.core.model.report;

import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.params.IPeriodQueryRequestParams;

public interface IPrintsReportParams extends IPeriodQueryRequestParams {

  Class getPrintClass();

  Integer getIdPatient();

  Integer getIdPayer();

  Person getPatient();

  AbstractParty getPayer();
}
