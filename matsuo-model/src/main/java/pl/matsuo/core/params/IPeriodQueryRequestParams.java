package pl.matsuo.core.params;

import java.util.Date;

public interface IPeriodQueryRequestParams extends IQueryRequestParams {

  Date getStartDate();

  Date getEndDate();
}
