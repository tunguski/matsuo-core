package pl.matsuo.core.service.permission.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permissions {

  private Map<String, List<String>> functions;

  /** Nazwa zbioru funkcji -> zbiór funkcji -> dla każdej lista typów zapytań */
  private Map<String, Map<String, List<String>>> functionSets;

  private Map<String, List<String>> permissions;
}
