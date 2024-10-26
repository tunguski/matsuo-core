package pl.matsuo.core.model.query;

public enum MatchMode {
  ANYWHERE,
  START,
  END;

  public String toMatchString(String text) {
    if (this == ANYWHERE) {
      return "%" + text + "%";
    } else if (this == START) {
      return text + "%";
    } else if (this == END) {
      return "%" + text;
    } else {
      throw new RuntimeException("Unhandled MatchMode " + this.name());
    }
  }
}
