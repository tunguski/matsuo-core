package pl.matsuo.core.web.controller.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleParty {

  private Long id;
  private String name;
  private Class type;
}
