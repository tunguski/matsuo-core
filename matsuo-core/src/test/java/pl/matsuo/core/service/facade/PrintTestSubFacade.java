package pl.matsuo.core.service.facade;

import java.math.BigDecimal;
import pl.matsuo.core.model.print.IPrintElementFacade;
import pl.matsuo.core.model.print.IPrintFacade;

public interface PrintTestSubFacade extends IPrintFacade<IPrintElementFacade> {

  BigDecimal getBigDecimal();

  void setBigDecimal(BigDecimal value);

  BigDecimal getSubBigDecimal();

  void setSubBigDecimal(BigDecimal value);
}
