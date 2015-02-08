package pl.matsuo.core.model.report;

import pl.matsuo.core.params.IPeriodQueryRequestParams;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;

import java.util.Date;


public interface IPrintsReportParams extends IPeriodQueryRequestParams {

  Class getPrintClass();

  Integer getIdPatient();
  Integer getIdPayer();

  Person getPatient();
  AbstractParty getPayer();
}

