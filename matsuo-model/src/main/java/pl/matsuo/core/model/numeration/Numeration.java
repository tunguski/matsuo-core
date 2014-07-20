package pl.matsuo.core.model.numeration;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.TemporalType.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Entity
public class Numeration extends AbstractEntity {


  protected Integer value;
  @NotNull
  protected Integer minValue;
  protected Integer maxValue;
  protected String code;
  protected String pattern;
  @Temporal(DATE)
  private Date identificationDate;


  public Integer getValue() {
    return value;
  }
  public void setValue(Integer value) {
    this.value = value;
  }
  public String getPattern() {
    return pattern;
  }
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public Integer getMaxValue() {
    return maxValue;
  }
  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }
  public Integer getMinValue() {
    return minValue;
  }
  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }
  public Date getIdentificationDate() {
    return identificationDate;
  }
  public void setIdentificationDate(Date identificationDate) {
    this.identificationDate = identificationDate;
  }
}

