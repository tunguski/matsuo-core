package pl.matsuo.core.service.permission;

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
