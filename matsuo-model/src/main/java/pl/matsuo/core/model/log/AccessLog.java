package pl.matsuo.core.model.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Getter
@Setter
public class AccessLog extends AbstractEntity {

  @Column(length = 512)
  String request;

  String method;

  @Column(length = 4096)
  String parameters;

  String ip;
  Integer status;
  Long idUser;
}
