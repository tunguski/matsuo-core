package pl.matsuo.core.model.report;

import pl.matsuo.core.IQueryRequestParams;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;

import java.util.Date;


public interface IPrintsReportParams extends IQueryRequestParams {

  Class getPrintClass();

  Date getStartDate();
  Date getEndDate();

  Integer getIdPatient();
  Integer getIdPayer();

  Person getPatient();
  AbstractParty getPayer();
}

