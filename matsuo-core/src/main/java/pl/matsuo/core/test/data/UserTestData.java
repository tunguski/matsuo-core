package pl.matsuo.core.test.data;

import static pl.matsuo.core.model.user.GroupEnum.ADMIN;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.core.conf.DiscoverTypes;

@Component
@Order(0)
@DiscoverTypes({GroupTestData.class})
public class UserTestData extends AbstractUserTestData {

  @Override
  public void execute() {
    createUser("Vlad", "Hernandez", "admin", "6%86P#WnukNp2gBm", ADMIN.name());
  }
}
