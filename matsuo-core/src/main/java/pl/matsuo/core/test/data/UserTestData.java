package pl.matsuo.core.test.data;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.core.conf.DiscoverTypes;

import static pl.matsuo.core.model.user.GroupEnum.*;


@Component
@Order(0)
@DiscoverTypes({ GroupTestData.class })
public class UserTestData extends AbstractUserTestData {


  @Override
  public void execute() {
    createUser("Administrator", "IT", "admin", "test", ADMIN.name());
  }
}

