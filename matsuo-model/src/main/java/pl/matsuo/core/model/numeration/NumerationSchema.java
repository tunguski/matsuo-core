package pl.matsuo.core.model.numeration;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;


/**
 * Created by tunguski on 2014-07-13
 */
@Entity
public class NumerationSchema extends AbstractEntity {


  protected Integer value;
  @NotNull
  protected Integer minValue;
  protected Integer maxValue;
  protected String code;
  protected String pattern;
  /**
   * How not numeration instances should be created basing on data contained in this schema.
   */
  protected String creationStrategy;


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
  public String getCreationStrategy() {
    return creationStrategy;
  }
  public void setCreationStrategy(String creationStrategy) {
    this.creationStrategy = creationStrategy;
  }
}

