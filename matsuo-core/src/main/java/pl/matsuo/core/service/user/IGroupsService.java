package pl.matsuo.core.service.user;

import java.util.Set;
import pl.matsuo.core.model.organization.AbstractParty;

/** Created by tunguski on 21.12.13. */
public interface IGroupsService {

  /** Returns list of parties that qualify by group name. */
  Set<AbstractParty> findActualElements(String groupName);
}
