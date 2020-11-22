package pl.matsuo.core.util.desktop.showcase.view;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.hr;
import static j2html.TagCreator.input;
import static j2html.TagCreator.li;
import static j2html.TagCreator.ol;
import static j2html.TagCreator.text;

import j2html.tags.ContainerTag;
import lombok.RequiredArgsConstructor;
import pl.matsuo.core.util.desktop.component.ViewComponents;
import pl.matsuo.core.util.desktop.mvc.IRequest;
import pl.matsuo.core.util.desktop.mvc.IView;

@RequiredArgsConstructor
public class BasicMenuView implements IView<IRequest, Object> {

  final ViewComponents viewComponents;

  @Override
  public ContainerTag view(IRequest request, Object model) {
    return viewComponents.pageTemplate(
        "Application menu",
        viewComponents.rowCol(
            h1("Menu"),
            hr(),
            form(div(
                    attrs(".input-group"),
                    input(attrs(".form-control")).withType("text"),
                    div(
                        attrs(".input-group-append"),
                        button(attrs(".btn.btn-outline-secondary"), text("Search"))
                            .withType("submit"))))
                .withMethod("post")
                .withAction("#search"),
            hr(),
            ol(
                attrs(".list-unstyled"),
                li(a("One").withHref("/one")),
                li(a("Two").withHref("/two")),
                li(a("Three").withHref("/three")))));
  }
}
