package pl.matsuo.core.service.user;

import pl.matsuo.core.model.organization.AbstractParty;

import java.util.Set;


/**
 * Created by tunguski on 21.12.13.
 */
public interface IGroupsService {


  /**
   * Returns list of parties that qualify by group name.
   */
  Set<AbstractParty> findActualElements(String groupName);
}
