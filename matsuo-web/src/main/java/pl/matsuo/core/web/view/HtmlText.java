package pl.matsuo.core.web.view;

public class HtmlText extends HtmlPart {

  protected String text;

  public HtmlText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
