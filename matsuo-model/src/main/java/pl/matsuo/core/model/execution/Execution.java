package pl.matsuo.core.model.execution;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import java.util.Date;


/**
 * Model bazodanowy dla zapisów o wykonaniach serwisów.
 * @author Marek Romanowski
 * @since 11-07-2013
 */
@Entity
public class Execution extends AbstractEntity {


  private Date runDate;
  private boolean success;
  private String beanName;


  // getters
  public Date getRunDate() {
    return runDate;
  }
  public void setRunDate(Date runDate) {
    this.runDate = runDate;
  }
  public boolean getSuccess() {
    return success;
  }
  public void setSuccess(boolean success) {
    this.success = success;
  }
  public String getBeanName() {
    return beanName;
  }
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }
}

