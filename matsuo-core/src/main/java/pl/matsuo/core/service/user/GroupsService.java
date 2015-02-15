package pl.matsuo.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;


/**
 * Created by tunguski on 21.12.13.
 */
@Service
public class GroupsService implements IGroupsService {


  @Autowired
  Database database;
  Map<String, IGroupProviderService> groupProviderServiceMap;


  @Override
  public Set<AbstractParty> findActualElements(String groupName) {
    IGroupProviderService providerService = groupProviderServiceMap.get(groupName);

    Set<AbstractParty> elements = new HashSet<>();

    if (providerService != null) {
      elements.addAll(providerService.findActualElements());
    }

    return elements;
  }


  @Autowired(required = false)
  public void setReportServices(IGroupProviderService[] groupProviderServices) {
    groupProviderServiceMap = toMap(asList(groupProviderServices), "groupName");
  }
}

