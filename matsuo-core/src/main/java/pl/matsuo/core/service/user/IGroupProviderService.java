package pl.matsuo.core.service.user;

import pl.matsuo.core.model.organization.AbstractParty;

import java.util.Set;


/**
 * Created by tunguski on 21.12.13.
 */
public interface IGroupProviderService {


  /**
   * Zwraca listę podmiotów które aktualnie kwalifikują się do grupy.
   */
  Set<AbstractParty> findActualElements();


  /**
   * Nazwa grupy dla którą obsługuje dana instancja serwisu.
   */
  String getGroupName();
}

