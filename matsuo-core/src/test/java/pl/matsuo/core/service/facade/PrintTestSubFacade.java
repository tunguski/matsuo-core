package pl.matsuo.core.service.facade;

import pl.matsuo.core.model.print.IPrintElementFacade;
import pl.matsuo.core.model.print.IPrintFacade;

import java.math.BigDecimal;


public interface PrintTestSubFacade extends IPrintFacade<IPrintElementFacade> {


  BigDecimal getBigDecimal();
  void setBigDecimal(BigDecimal value);

  BigDecimal getSubBigDecimal();
  void setSubBigDecimal(BigDecimal value);
}

