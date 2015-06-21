package pl.matsuo.core.web.controller.organization;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.util.ComparatorUtil;
import pl.matsuo.core.web.controller.AbstractSearchController;
import pl.matsuo.core.web.controller.AbstractSimpleController;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.*;
import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.matsuo.core.util.ComparatorUtil.comparator;


@RestController
@RequestMapping("/payers")
public class PayerController extends AbstractSearchController<AbstractParty, IQueryRequestParams> {


  @Override
  protected <F extends AbstractEntity> List<Function<F, String>> queryMatchers(Class<F> entity) {
    if (Person.class.equals(entity)) {
      List<Function<Person, String>> result = asList(Person::getFirstName, Person::getLastName);
      return (List) result;
    } else if (OrganizationUnit.class.equals(entity)) {
      List<Function<OrganizationUnit, String>> result =
          asList(OrganizationUnit::getFullName, OrganizationUnit::getShortName, OrganizationUnit::getCode);
      return (List) result;
    } else {
      return emptyList();
    }
  }


  @RequestMapping(method = GET)
  public List<AbstractParty> list(IQueryRequestParams params) {
    List<Person> persons = super.list(Person.class, params);
    List<OrganizationUnit> companies = super.list(OrganizationUnit.class, params);

    List<AbstractParty> parties = new ArrayList<>();
    parties.addAll(persons);
    parties.addAll(companies);
    parties.sort(comparator(AbstractParty::getName));

    return parties;
  }
}

