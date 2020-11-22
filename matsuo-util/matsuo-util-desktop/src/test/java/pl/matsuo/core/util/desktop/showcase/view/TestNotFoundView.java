package pl.matsuo.core.util.desktop.showcase.view;

import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.ViewTestUtil.storeView;
import static pl.matsuo.core.util.desktop.mvc.IRequest.request;

import org.junit.Test;
import pl.matsuo.core.util.desktop.component.ViewComponents;

public class TestNotFoundView {

  @Test
  public void view() {
    NotFoundView view = new NotFoundView(new ViewComponents());
    String rendered = view.view(request("/very_strange_path", emptyMap()), null).renderFormatted();

    storeView("notFound.html", rendered);
  }
}
