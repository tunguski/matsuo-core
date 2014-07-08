package pl.matsuo.core.model.report;

import pl.matsuo.core.IRequestParams;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;

import java.util.Date;


public interface IPrintsReportParams extends IRequestParams {

  Class getPrintClass();

  Date getStartDate();
  Date getEndDate();

  Integer getIdPatient();
  Integer getIdPayer();

  Person getPatient();
  AbstractParty getPayer();
}

