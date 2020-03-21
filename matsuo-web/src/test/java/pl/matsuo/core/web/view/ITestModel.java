package pl.matsuo.core.web.view;

import java.sql.Time;
import java.util.Date;
import javax.persistence.EnumType;

public interface ITestModel {

  // getters
  Integer getReference();

  void setReference(Integer reference);

  @PatternHavingAnnotation
  Integer getDuration();

  void setDuration(Integer duration);

  TestReferenceModel getSubModel();

  void setSubModel(TestReferenceModel subModel);

  String getText();

  void setText(String text);

  Date getDate();

  void setDate(Date date);

  boolean getBool();

  void setBool(boolean bool);

  Time getTime();

  void setTime(Time time);

  EnumType getEnumValue();

  void setEnumValue(EnumType enumValue);
}
