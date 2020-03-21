package pl.matsuo.core.service.user;

import java.util.Set;
import pl.matsuo.core.model.organization.AbstractParty;

public interface IGroupProviderService {

  /** Zwraca listę podmiotów które aktualnie kwalifikują się do grupy. */
  Set<AbstractParty> findActualElements();

  /** Nazwa grupy dla którą obsługuje dana instancja serwisu. */
  String getGroupName();
}
