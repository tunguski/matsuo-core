package pl.matsuo.core.model.user.initializer;

import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.user.User;

public class UserInitializer implements Initializer<User> {

  @Override
  public void init(User user) {
    user.getGroups().size();
  }
}
