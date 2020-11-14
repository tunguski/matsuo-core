package pl.matsuo.core.util.desktop.showcase.view;

import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.IRequest.request;
import static pl.matsuo.core.util.desktop.ViewTestUtil.storeView;

import org.junit.Test;
import pl.matsuo.core.util.desktop.component.ViewComponents;

public class TestSampleView {

  @Test
  public void view() {
    SampleView view = new SampleView(new ViewComponents());
    String rendered = view.view(request("/sample", emptyMap()), null).renderFormatted();

    storeView("sampleView.html", rendered);
  }
}
