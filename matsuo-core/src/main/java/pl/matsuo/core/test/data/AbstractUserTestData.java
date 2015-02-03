package pl.matsuo.core.test.data;

import org.springframework.util.Assert;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;

import java.util.List;
import java.util.Objects;

import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.SecurityUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


@DiscoverTypes({ GroupTestData.class })
public abstract class AbstractUserTestData extends AbstractTestData {


  protected void createUser(String firstName, String lastName, String username, String password, String ... groupNames) {
    Person person = database.findOne(query(Person.class, eq("firstName", firstName), eq("lastName", lastName)));
    if (person == null) {
      person = new Person();
      person.setFirstName(firstName);
      person.setLastName(lastName);
      person.setPesel("00000000000");
      person.setBirthDate(date(1972, 4, 21));

      person.setAddress(with(new Address(), address -> {
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

    List<Group> groups = database.find(query(Group.class, in("name", groupNames)));

    Assert.isTrue(Objects.equals(groups.size(), groupNames.length));

    user.getGroups().addAll(groups);
    user.setPerson(person);

    database.create(user);
  }
}

