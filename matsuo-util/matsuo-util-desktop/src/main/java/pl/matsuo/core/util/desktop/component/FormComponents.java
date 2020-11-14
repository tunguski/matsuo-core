package pl.matsuo.core.util.desktop.component;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.text;

import j2html.tags.ContainerTag;
import pl.matsuo.core.util.desktop.IRequest;

public class FormComponents {

  public static final String PRIMARY = "primary";

  public ContainerTag textField(String s, String name, IRequest request) {
    return div(
        attrs(".form-group"),
        label(s).attr("for", "form-" + name),
        input(attrs(".form-control"))
            .withType("text")
            .withName(name)
            .withId("form-" + name)
            .withCondValue(request.hasParam(name), request.getParam(name)));
  }

  public ContainerTag submitButton(String label) {
    return submitButton(label, PRIMARY);
  }

  public ContainerTag submitButton(String label, String type) {
    return button(attrs(".btn.btn-" + type), text(label)).withType("submit");
  }
}
