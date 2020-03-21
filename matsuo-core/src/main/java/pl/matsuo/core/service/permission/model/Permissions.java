package pl.matsuo.core.service.permission.model;

import java.util.List;
import java.util.Map;

/** Created by tunguski on 21.12.13. */
public class Permissions {

  private Map<String, List<String>> functions;
  /** Nazwa zbioru funkcji -> zbiór funkcji -> dla każdej lista typów zapytań */
  private Map<String, Map<String, List<String>>> functionSets;

  private Map<String, List<String>> permissions;

  public Map<String, List<String>> getFunctions() {
    return functions;
  }

  public void setFunctions(Map<String, List<String>> functions) {
    this.functions = functions;
  }

  public Map<String, Map<String, List<String>>> getFunctionSets() {
    return functionSets;
  }

  public void setFunctionSets(Map<String, Map<String, List<String>>> functionSets) {
    this.functionSets = functionSets;
  }

  public Map<String, List<String>> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, List<String>> permissions) {
    this.permissions = permissions;
  }
}
