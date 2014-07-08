package pl.matsuo.core.model.user;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by tunguski on 20.12.13.
 */
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

