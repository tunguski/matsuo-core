package pl.matsuo.core.test.data;

import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.in;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.DateUtil.date;
import static pl.matsuo.core.util.SecurityUtil.passwordHash;
import static pl.matsuo.core.util.function.FunctionalUtil.with;

import java.util.List;
import org.springframework.util.Assert;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;

@DiscoverTypes({GroupTestData.class})
public abstract class AbstractUserTestData extends AbstractTestData {

  protected void createUser(
      String firstName, String lastName, String username, String password, String... groupNames) {
    Person person =
        database.findOne(
            query(
                Person.class,
                eq(Person::getFirstName, firstName),
                eq(Person::getLastName, lastName)));
    if (person == null) {
      person = new Person();
      person.setFirstName(firstName);
      person.setLastName(lastName);
      person.setPesel("00000000000");
      person.setBirthDate(date(1972, 4, 21));

      person.setAddress(
          with(
              new Address(),
              address -> {
                address.setStreet("Wałbrzyska");
                address.setApartmentNumber("20");
                address.setHouseNumber("32");
                address.setTown("Warszawa");
              }));
    }

    database.create(person);

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordHash(password));

    List<Group> groups = database.find(query(Group.class, in(Group::getName, groupNames)));

    Assert.isTrue(
        groups.size() == groupNames.length,
        "Incorrect number of groups " + groups + " expected " + groupNames);

    user.getGroups().addAll(groups);
    user.setPerson(person);

    database.create(user);
  }
}
