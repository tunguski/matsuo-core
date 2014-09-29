package pl.matsuo.core.web.view;

import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.model.validation.PasswordField;

import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.util.Date;


public class TestModel {


  @EntityReference(value = TestReferenceModel.class)
  public Integer reference;
  @ManyToOne
  public TestReferenceModel subModel;
  public Integer duration;
  public String text;
  @PasswordField
  public String password;
  public Date date;
  public Time time;
  public EnumType enumValue;
  public boolean bool;


  // getters
  public Integer getReference() {
    return reference;
  }
  public void setReference(Integer reference) {
    this.reference = reference;
  }
  public Integer getDuration() {
    return duration;
  }
  public void setDuration(Integer duration) {
    this.duration = duration;
  }
  public TestReferenceModel getSubModel() {
    return subModel;
  }
  public void setSubModel(TestReferenceModel subModel) {
    this.subModel = subModel;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public boolean getBool() {
    return bool;
  }
  public void setBool(boolean bool) {
    this.bool = bool;
  }
  public Time getTime() {
    return time;
  }
  public void setTime(Time time) {
    this.time = time;
  }
  public EnumType getEnumValue() {
    return enumValue;
  }
  public void setEnumValue(EnumType enumValue) {
    this.enumValue = enumValue;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}

