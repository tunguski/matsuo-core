package pl.matsuo.core.web.view;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.validation.EntityReference;

import java.util.Date;


public class TestReferenceModel extends AbstractEntity {


  Date date;


  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }

  @EntityReference(value = TestReferenceModel.class)
  @Override
  public Integer getId() {
    return super.getId();
  }
}
