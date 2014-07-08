package pl.matsuo.core.kv;

import pl.matsuo.core.model.kv.IKeyValueFacade;

import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public interface ITestKeyValueFacade extends IKeyValueFacade {

  String getString();
  void setString(String value);

  EnumType getEnumType();
  void setEnumType(EnumType value);

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

  void setDoubleToInteger(Double value);
  Integer getDoubleToInteger();

  void setDoubleToBigDecimal(Double value);
  BigDecimal getDoubleToBigDecimal();

  List<ITestKeyValueFacade> getInternalElements();
}

