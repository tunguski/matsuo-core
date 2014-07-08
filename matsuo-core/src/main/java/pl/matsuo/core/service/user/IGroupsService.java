package pl.matsuo.core.service.user;

import pl.matsuo.core.model.organization.AbstractParty;

import java.util.Set;


/**
 * Created by tunguski on 21.12.13.
 */
public interface IGroupsService {


  /**
   * Zwraca listę podmiotów które aktualnie kwalifikują się do grupy.
   */
  Set<AbstractParty> findActualElements(String groupName);
}
