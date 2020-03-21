package pl.matsuo.core.web.controller.organization;

import static java.util.Arrays.*;

import java.util.List;
import java.util.function.Function;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.web.controller.AbstractSimpleController;

@RestController
@RequestMapping("/persons")
public class PersonController extends AbstractSimpleController<Person> {

  @Override
  protected List<Function<Person, String>> queryMatchers() {
    return asList(Person::getFirstName, Person::getLastName);
  }
}
