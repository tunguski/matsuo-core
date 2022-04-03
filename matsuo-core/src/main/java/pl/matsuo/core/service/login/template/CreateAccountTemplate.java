package pl.matsuo.core.service.login.template;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.meta;

import j2html.tags.specialized.BodyTag;
import pl.matsuo.core.service.login.model.LoginEmailData;
import pl.matsuo.core.service.template.ITemplate;

public class CreateAccountTemplate implements ITemplate<LoginEmailData> {

  @Override
  public String generate(LoginEmailData model) {
    return generate(getBody(model));
  }

  public static String generate(BodyTag body) {
    return "<!DOCTYPE html>"
        + html(
            head(
                meta()
                    .attr("http-equiv", "content-type")
                    .attr("content", "text/html; charset=utf-8"),
                meta()
                    .attr("name", "viewport")
                    .attr("content", "width=device-width, initial-scale=1")),
            body);
  }

  private BodyTag getBody(LoginEmailData model) {
    return body(
        div(h1("Account activation")),
        div(
            a("Click this link to activate your account")
                .withHref(
                    "http://accounting.matsuo-it.com/api/login/activateAccount/"
                        + model.getUser().getUnblockTicket())));
  }

  @Override
  public String defaultName() {
    return "createAccount";
  }
}
