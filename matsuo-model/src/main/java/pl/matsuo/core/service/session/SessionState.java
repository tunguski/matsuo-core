package pl.matsuo.core.service.session;

import static java.lang.System.currentTimeMillis;
import static pl.matsuo.core.model.user.GroupEnum.GUEST;
import static pl.matsuo.core.model.user.GroupEnum.USER;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;

@Component
@Scope(value = "wideSession")
@Getter
@Setter
public class SessionState {

  private User user;
  private Long idBucket;
  private long lastRequestTime;
  private long loginTime;
  private Set<String> userGroups = new HashSet<>();

  public SessionState() {
    setUser(null);
  }

  public void setUser(User user) {
    this.user = user;
    idBucket = user != null ? user.getIdBucket() : null;

    userGroups.clear();
    if (user == null) {
      userGroups.add(GUEST.name());
      lastRequestTime = 0;
    } else {
      userGroups.add(USER.name());
      for (Group group : user.getGroups()) {
        userGroups.add(group.getName());
      }
      lastRequestTime = currentTimeMillis();
    }

    loginTime = lastRequestTime;
  }

  public boolean isInGroup(String groupName) {
    return userGroups.contains(groupName);
  }
}
