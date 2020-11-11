package pl.matsuo.core.util.desktop.showcase.view;

import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.IRequest.request;
import static pl.matsuo.core.util.desktop.ViewTestUtil.storeView;

import junit.framework.TestCase;
import org.junit.Test;
import pl.matsuo.core.util.desktop.ViewComponents;

public class TestBasicMenuView extends TestCase {

  @Test
  public void testGenerateView() {
    BasicMenuView view = new BasicMenuView(new ViewComponents());
    String rendered = view.view(request("/menu", emptyMap()), null).renderFormatted();

    storeView("basicMenu.html", rendered);
  }
}
