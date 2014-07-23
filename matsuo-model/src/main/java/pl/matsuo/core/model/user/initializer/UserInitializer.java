package pl.matsuo.core.model.user.initializer;

import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.user.User;


/**
 * Created by tunguski on 14.01.14.
 */
public class UserInitializer implements Initializer<User> {


  @Override
  public void init(User user) {
    user.getGroups().size();
  }
}

