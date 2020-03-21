package pl.matsuo.core.test.data;

import org.springframework.stereotype.Component;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.GroupEnum;

@Component
public class GroupTestData extends AbstractTestData {

  @Override
  public void execute() {
    for (GroupEnum group : GroupEnum.values()) {
      createGroup(group.name());
    }
  }

  protected void createGroup(String groupName) {
    Group group = new Group();
    group.setName(groupName);
    database.create(group);
  }
}
