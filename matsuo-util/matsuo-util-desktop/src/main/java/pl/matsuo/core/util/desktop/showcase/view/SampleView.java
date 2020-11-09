package pl.matsuo.core.util.desktop.showcase.view;

import static j2html.TagCreator.a;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.hr;
import static j2html.TagCreator.p;
import static j2html.TagCreator.td;
import static j2html.TagCreator.text;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;
import static java.util.Arrays.asList;

import j2html.tags.ContainerTag;
import lombok.RequiredArgsConstructor;
import pl.matsuo.core.util.desktop.IRequest;
import pl.matsuo.core.util.desktop.IView;
import pl.matsuo.core.util.desktop.ViewComponents;

@RequiredArgsConstructor
public class SampleView implements IView<IRequest> {

  final ViewComponents viewComponents;

  @Override
  public ContainerTag view(IRequest model) {
    return viewComponents.pageTemplate(
        "Menu",
        viewComponents.rowCol(
            h1("Main title"),
            hr(),
            p(text("This is content"), a("And now we go...").withHref("/menu")),
            viewComponents.table(
                tr(th("No."), th("Content")),
                asList(1, 2, 3, 4),
                elem -> tr(td("" + elem), td("" + elem)))));
  }
}
