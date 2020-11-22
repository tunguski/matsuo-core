package pl.matsuo.core.util.desktop.showcase.view;

import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.ViewTestUtil.storeView;
import static pl.matsuo.core.util.desktop.mvc.IRequest.request;

import junit.framework.TestCase;
import org.junit.Test;
import pl.matsuo.core.util.desktop.component.ViewComponents;

public class TestBasicMenuView extends TestCase {

  @Test
  public void testGenerateView() {
    BasicMenuView view = new BasicMenuView(new ViewComponents());
    String rendered = view.view(request("/menu", emptyMap()), null).renderFormatted();

    storeView("basicMenu.html", rendered);
  }
}
