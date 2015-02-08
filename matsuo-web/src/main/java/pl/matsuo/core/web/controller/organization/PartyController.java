package pl.matsuo.core.web.controller.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.service.db.Database;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 20.12.13.
 */
@RestController
@RequestMapping("/party")
public class PartyController {


  @Autowired
  protected Database database;
  @Autowired
  protected PayerController payerController;


  @RequestMapping(method = GET)
  public List<SimpleParty> list(IQueryRequestParams params) {
    List<SimpleParty> result = new ArrayList<>();

    List<AbstractParty> parties = payerController.list(params);
    for (AbstractParty party : parties) {
      result.add(new SimpleParty(party.getId(), party.getName(), AbstractParty.class));
    }

    List<Group> groups = database.find(query(Group.class, ilike("name", params.getQuery())));
    for (Group group : groups) {
      result.add(new SimpleParty(group.getId(), group.getName(), Group.class));
    }

    return result;
  }
}

