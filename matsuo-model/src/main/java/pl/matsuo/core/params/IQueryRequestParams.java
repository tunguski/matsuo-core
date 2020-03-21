package pl.matsuo.core.params;

/** Created by tunguski on 23.11.13. */
public interface IQueryRequestParams extends ISearchRequestParams {

  /**
   * Search query provided by user to form's search input. It is used to create general searches,
   * like people by name or identification document's number.
   */
  String getQuery();
}
