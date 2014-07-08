package pl.matsuo.core.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.*;


public class HtmlElement extends HtmlPart {


  private final String element;
  private final HtmlPart[] innerElements;
  private final List<String> classes = new ArrayList<>();
  private final Map<String, String> attributes = new TreeMap<>();


  public HtmlElement(String element, HtmlPart ... innerElements) {
    this.element = element;
    this.innerElements = innerElements;
  }


  public HtmlElement style(String ... classes) {
    return style(asList(classes));
  }


  public HtmlElement style(List<String> classes) {
    this.classes.addAll(classes);
    return this;
  }


  public HtmlElement attr(String name, String value) {
    attributes.put(name, value);
    return this;
  }


  public String getAttr(String name) {
    return attributes.get(name);
  }


  @Override
  public String toString() {
    String rendering = "<" + element;

    rendering += " class=\"";
    for (String clazz : classes) {
      rendering += " " + clazz;
    }
    rendering += "\"";

    for (String attrName : attributes.keySet()) {
      String value = attributes.get(attrName);
      rendering += " " + attrName + (value == null ? "" : "=\"" + attributes.get(attrName) + "\"");
    }

    rendering += ">\n";

    for (HtmlPart htmlPart : innerElements) {
      rendering += htmlPart.toString() + "\n";
    }

    rendering += "</" + element + ">";

    return rendering;
  }
}

