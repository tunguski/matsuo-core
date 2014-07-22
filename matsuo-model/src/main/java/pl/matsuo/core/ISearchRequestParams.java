package pl.matsuo.core;

import pl.matsuo.core.IRequestParams;


public interface ISearchRequestParams extends  IRequestParams {

  /**
   * Number of elements to be returned by search.
   */
  Integer getLimit();

  /**
   * Paging offset.
   */
  Integer getOffset();
}
