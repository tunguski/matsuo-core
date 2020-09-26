package pl.matsuo.core.web.controller.organization;

public class SimpleParty {

  private Long id;
  private String name;
  private Class type;

  public SimpleParty(Long id, String name, Class type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public SimpleParty() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
