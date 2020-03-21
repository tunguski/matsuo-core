package pl.matsuo.core.params;

public interface ISearchRequestParams extends IRequestParams {

  /** Number of elements to be returned by search. */
  Integer getLimit();

  /** Paging offset. */
  Integer getOffset();
}
