package pl.matsuo.core.web.controller.organization;

/** Created by tunguski on 20.12.13. */
public class SimpleParty {

  private Integer id;
  private String name;
  private Class type;

  public SimpleParty(Integer id, String name, Class type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public SimpleParty() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class getType() {
    return type;
  }

  public void setType(Class type) {
    this.type = type;
  }
}
