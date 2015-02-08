package pl.matsuo.core.params;

import java.util.Date;

/**
 * Created by marek on 08.02.15.
 */
public interface IPeriodQueryRequestParams extends IQueryRequestParams {

  Date getStartDate();
  Date getEndDate();
}
