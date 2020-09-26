package pl.matsuo.core.web.controller.organization;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.organization.initializer.CompanyInitializer;
import pl.matsuo.core.web.controller.AbstractSimpleController;

/**
 * Organization unit controller.
 *
 * @since Jul 18, 2013
 */
@RestController
@RequestMapping("/organizationUnits")
public class OrganizationUnitController extends AbstractSimpleController<OrganizationUnit> {

  @Override
  protected List<Function<OrganizationUnit, String>> queryMatchers() {
    return asList(OrganizationUnit::getFullName);
  }

  @Override
  protected List<? extends Initializer<OrganizationUnit>> entityInitializers() {
    return asList(new CompanyInitializer());
  }

  @Override
  @RequestMapping(value = "/{id}", method = GET)
  public HttpEntity<OrganizationUnit> find(@PathVariable("id") Long id) {
    HttpEntity<OrganizationUnit> entity = super.find(id);
    entity.getBody().getEmployees().size();
    return entity;
  }

  @RequestMapping(value = "/{id}/employee/{idEmployee}", method = POST)
  @ResponseStatus(OK)
  public Person addEmployee(
      @PathVariable("id") Long id, @PathVariable("idEmployee") Long idEmployee) {
    Person person = database.findById(Person.class, idEmployee);
    OrganizationUnit organizationUnit =
        database.findById(OrganizationUnit.class, id, entityInitializers);

    organizationUnit.getEmployees().add(person);
    database.update(organizationUnit);

    return person;
  }

  @RequestMapping(value = "/{id}/employee/{idEmployee}", method = DELETE)
  @ResponseStatus(OK)
  public void removeEmployee(
      @PathVariable("id") Long id, @PathVariable("idEmployee") Long idEmployee) {
    OrganizationUnit organizationUnit =
        database.findById(OrganizationUnit.class, id, entityInitializers);

    Iterator<Person> iterator = organizationUnit.getEmployees().iterator();
    while (iterator.hasNext()) {
      Person person = iterator.next();
      if (person.getId().equals(idEmployee)) {
        iterator.remove();
      }
    }

    database.update(organizationUnit);
  }
}
