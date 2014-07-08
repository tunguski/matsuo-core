package pl.matsuo.core.service.facade;

import pl.matsuo.core.model.print.IPrintElementFacade;
import pl.matsuo.core.model.print.IPrintFacade;

import java.math.BigDecimal;
import java.util.Date;


public interface PrintTestFacade extends IPrintFacade<IPrintElementFacade> {

  String getString();
  void setString(String value);

  Integer getInteger();
  void setInteger(Integer value);

  Double getDouble();
  void setDouble(Double value);

  Float getFloat();
  void setFloat(Float value);

  Date getDate();
  void setDate(Date value);

  BigDecimal getBigDecimal();
  void setBigDecimal(BigDecimal value);
  
  Boolean getBoolean();
  void setBoolean(Boolean value);

  PrintTestSubFacade getSubEntity();
}

