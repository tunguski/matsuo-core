package pl.matsuo.core.test.data;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.organization.OrganizationUnit;

import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Component
@Order(20)
@DiscoverTypes({ PayersTestData.class })
public class NumerationTestData extends AbstractTestData {


  protected OrganizationUnit mediq;


  @Override
  public void execute() {
    mediq = database.findOne(query(OrganizationUnit.class, eq("code", PayersTestData.MEDIQ)));

    createNumeration("INVOICE", "FV/2013/$");
    createNumeration("RECEIPT", "PAR/2013/$");

    createNumeration("CORRECTIVE_INVOICE", "FV-K/2013/$");
    createNumeration("CORRECTIVE_RECEIPT", "PAR-K/2013/$");

    createNumeration("WITHDRAW_SLIP", "KW/2013/$");
    createNumeration("DEPOSIT_SLIP", "KP/2013/$");
  }


  private Numeration createNumeration(String code, String pattern) {
    Numeration numeration = new Numeration();
    numeration.setIdEntity(mediq.getId());
    numeration.setValue(1);
    numeration.setMinValue(1);
    numeration.setCode(code);
    numeration.setPattern(pattern);
    database.create(numeration);

    return numeration;
  }


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}

