package pl.matsuo.core.service.user;

import java.util.Set;
import pl.matsuo.core.model.organization.AbstractParty;

public interface IGroupsService {

  /** Returns list of parties that qualify by group name. */
  Set<AbstractParty> findActualElements(String groupName);
}
