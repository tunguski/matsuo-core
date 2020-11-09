package pl.matsuo.core.util.desktop.showcase.view;

import static j2html.TagCreator.b;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.hr;

import j2html.tags.ContainerTag;
import lombok.RequiredArgsConstructor;
import pl.matsuo.core.util.desktop.IRequest;
import pl.matsuo.core.util.desktop.IView;
import pl.matsuo.core.util.desktop.ViewComponents;

@RequiredArgsConstructor
public class NotFoundView implements IView<IRequest> {

  final ViewComponents viewComponents;

  @Override
  public ContainerTag view(IRequest model) {
    return viewComponents.pageTemplate(
        "Page not found",
        viewComponents.rowCol(h1("Page not found"), hr(), b("Missing: '" + model.getPath() + "'")));
  }
}
