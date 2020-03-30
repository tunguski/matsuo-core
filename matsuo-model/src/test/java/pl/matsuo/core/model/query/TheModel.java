package pl.matsuo.core.model.query;

import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

@Getter
@Setter
public class TheModel extends AbstractEntity {

  String test;
  Integer field;
  Integer f1;
  Integer f2;

  TheModel subModel;
}
