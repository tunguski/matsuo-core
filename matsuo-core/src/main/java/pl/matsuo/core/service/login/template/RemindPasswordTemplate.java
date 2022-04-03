package pl.matsuo.core.service.login.template;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;

import j2html.tags.specialized.BodyTag;
import pl.matsuo.core.service.login.model.LoginEmailData;
import pl.matsuo.core.service.template.ITemplate;

public class RemindPasswordTemplate implements ITemplate<LoginEmailData> {

  @Override
  public String generate(LoginEmailData model) {
    return CreateAccountTemplate.generate(getBody(model));
  }

  private BodyTag getBody(LoginEmailData model) {
    return body(
        div(h1("Remind password")),
        div(
            a("Click this link to reset your password")
                .withHref(
                    "http://accounting.matsuo-it.com/api/login/activateAccount/"
                        + model.getUser().getUnblockTicket())));
  }

  @Override
  public String defaultName() {
    return "remindPassword";
  }
}
