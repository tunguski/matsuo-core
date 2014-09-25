package pl.matsuo.core.model.numeration;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.TemporalEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.TemporalType.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames={ "idBucket", "code", "startDate" })
})
public class Numeration extends AbstractEntity implements TemporalEntity {


  @NotNull
  protected Integer value;
  /**
   * Owning entity id. May be null if this numeration is 'general' (depending on the context).
   */
  protected Integer idEntity;
  @NotNull
  protected Integer minValue;
  protected Integer maxValue;
  @NotNull
  protected String code;
  @NotNull
  protected String pattern;
  @Temporal(DATE)
  private Date startDate;
  @Temporal(DATE)
  private Date endDate;


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
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public Integer getIdEntity() {
    return idEntity;
  }
  public void setIdEntity(Integer idEntity) {
    this.idEntity = idEntity;
  }
}

