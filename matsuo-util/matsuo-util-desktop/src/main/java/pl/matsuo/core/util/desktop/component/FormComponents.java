package pl.matsuo.core.util.desktop.component;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.text;
import static java.util.Arrays.asList;

import j2html.tags.ContainerTag;
import pl.matsuo.core.util.collection.Pair;
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

  public ContainerTag formAsLink(
      String linkText, String actionUrl, Pair<String, String>... hiddenInputs) {
    ContainerTag form =
        form(
            attrs(".d-inline-block.m-0"),
            each(
                asList(hiddenInputs),
                param ->
                    input()
                        .withType("hidden")
                        .withName(param.getKey())
                        .withValue(param.getValue())),
            button(attrs(".btn.btn-link.p-0.border-0.align-baseline"), text(linkText))
                .withType("submit"));

    if (actionUrl != null) {
      form = form.withAction(actionUrl);
    }

    return form.withMethod("post");
  }
}
