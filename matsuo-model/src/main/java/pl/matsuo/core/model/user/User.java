package pl.matsuo.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.validation.PasswordField;

/** Created by tunguski on 20.12.13. */
@Entity
@Table(name = "tblUser")
public class User extends AbstractEntity {

  @NotNull @Valid @OneToOne private Person person;

  @NotNull
  @Column(unique = true)
  private String username;

  @NotNull @PasswordField private String password;
  private Date lastLoginTime;
  private Date lastPasswordChangeTime;
  private Integer nextPasswordChangeDays;
  private boolean blocked = false;
  /**
   * Unique ticket assigned to user that will unblock account if provided.
   *
   * <p>For sending in email for confirmation that email exists.
   */
  private String unblockTicket;

  @ManyToMany private final Set<Group> groups = new HashSet<>();

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Date getLastPasswordChangeTime() {
    return lastPasswordChangeTime;
  }

  public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
    this.lastPasswordChangeTime = lastPasswordChangeTime;
  }

  public Integer getNextPasswordChangeDays() {
    return nextPasswordChangeDays;
  }

  public void setNextPasswordChangeDays(Integer nextPasswordChangeDays) {
    this.nextPasswordChangeDays = nextPasswordChangeDays;
  }

  public Set<Group> getGroups() {
    return groups;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public boolean getBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public String getUnblockTicket() {
    return unblockTicket;
  }

  public void setUnblockTicket(String unblockTicket) {
    this.unblockTicket = unblockTicket;
  }
}
