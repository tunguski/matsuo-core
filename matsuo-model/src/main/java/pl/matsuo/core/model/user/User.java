package pl.matsuo.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.validation.PasswordField;

@Entity
@Table(name = "tblUser")
@Getter
@Setter
public class User extends AbstractEntity {

  @NotNull @Valid @OneToOne private Person person;

  @NotNull
  @Column(unique = true)
  private String username;

  @NotNull @PasswordField private String password;
  private Date lastLoginTime;
  private Date lastPasswordChangeTime;
  private Integer nextPasswordChangeDays;
  private Boolean blocked = false;

  /**
   * Unique ticket assigned to user that will unblock account if provided.
   *
   * <p>For sending in email for confirmation that email exists.
   */
  private String unblockTicket;

  @ManyToMany private final Set<Group> groups = new HashSet<>();

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }
}
