package pl.matsuo.core.util.desktop.showcase.view;

import static j2html.TagCreator.b;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.hr;

import j2html.tags.ContainerTag;
import lombok.RequiredArgsConstructor;
import pl.matsuo.core.util.desktop.component.ViewComponents;
import pl.matsuo.core.util.desktop.mvc.IRequest;
import pl.matsuo.core.util.desktop.mvc.IView;

@RequiredArgsConstructor
public class NotFoundView implements IView<IRequest, Object> {

  final ViewComponents viewComponents;

  @Override
  public ContainerTag view(IRequest request, Object model) {
    return viewComponents.pageTemplate(
        "Page not found",
        viewComponents.rowCol(
            h1("Page not found"), hr(), b("Missing: '" + request.getPath() + "'")));
  }
}
