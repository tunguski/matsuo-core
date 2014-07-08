package pl.matsuo.core.model.api;

import java.util.Date;

public interface TemporalEntity {

  Date getStartDate();
  Date getEndDate();
  void setStartDate(Date date);
  void setEndDate(Date date);
}
