package pl.matsuo.core.params;

public interface IQueryRequestParams extends ISearchRequestParams {

  /**
   * Search query provided by user to form's search input. It is used to create general searches,
   * like people by name or identification document's number.
   */
  String getQuery();
}
