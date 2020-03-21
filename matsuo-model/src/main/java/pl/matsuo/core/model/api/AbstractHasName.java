package pl.matsuo.core.model.api;

public class AbstractHasName implements IHasName {

  private String name;

  @Override
  public String name() {
    return name;
  }
  //
  //
  //  protected E val() {
  //    try {
  //      return (E) getClass().newInstance();
  //    } catch (Exception e) {
  //      throw new RuntimeException(e);
  //    }
  //  }
}
