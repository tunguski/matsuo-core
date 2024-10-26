package pl.matsuo.core.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Table(name = "tblGroup")
@Getter
@Setter
public class Group extends AbstractEntity {

  private String name;
}
