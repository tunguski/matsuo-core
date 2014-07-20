package pl.matsuo.core.service.session;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.*;
import static pl.matsuo.core.model.user.GroupEnum.*;


/**
 * Created by tunguski on 16.09.13.
 */
@Component
@Scope(value = "wideSession")
public class SessionState {


  private User user;
  private Integer idBucket;
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


  public Integer getIdBucket() {
    return idBucket;
  }


  public boolean isInGroup(String groupName) {
    return userGroups.contains(groupName);
  }


  public User getUser() {
    return user;
  }
  public long getLastRequestTime() {
    return lastRequestTime;
  }
  public void setLastRequestTime(long lastRequestTime) {
    this.lastRequestTime = lastRequestTime;
  }
  public long getLoginTime() {
    return loginTime;
  }
  public void setIdBucket(Integer idBucket) {
    this.idBucket = idBucket;
  }
}

