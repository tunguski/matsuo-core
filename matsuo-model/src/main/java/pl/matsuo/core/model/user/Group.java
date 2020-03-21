package pl.matsuo.core.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Table(name = "tblGroup")
public class Group extends AbstractEntity {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
