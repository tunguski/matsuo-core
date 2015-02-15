package pl.matsuo.core.model.query;

import pl.matsuo.core.model.AbstractEntity;


/**
 * Created by marek on 13.03.14.
 */
public class TheModel extends AbstractEntity {


  String test;
  Integer field;
  Integer f1;
  Integer f2;

  TheModel subModel;


  public String getTest() {
    return test;
  }
  public void setTest(String test) {
    this.test = test;
  }
  public TheModel getSubModel() {
    return subModel;
  }
  public void setSubModel(TheModel subModel) {
    this.subModel = subModel;
  }
  public Integer getField() {
    return field;
  }
  public void setField(Integer field) {
    this.field = field;
  }
  public Integer getF1() {
    return f1;
  }
  public void setF1(Integer f1) {
    this.f1 = f1;
  }
  public Integer getF2() {
    return f2;
  }
  public void setF2(Integer f2) {
    this.f2 = f2;
  }
}

