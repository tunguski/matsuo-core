package pl.matsuo.core.service.permission;

/** Created by tunguski on 21.12.13. */
public interface IPermissionService {

  public enum RequestType {
    GET,
    POST,
    PUT,
    DELETE,
    OPTIONS
  }

  /** Sprawdzenie uprawnień dla domyślego zapytania typu GET. */
  boolean isPermitted(String name);

  /** Sprawdzenie uprawnień dla zapytania wybranego typu. */
  boolean isPermitted(String requestName, RequestType requestType);

  void logoff();
}
