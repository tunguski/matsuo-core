package pl.matsuo.core.util.desktop.showcase;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import pl.matsuo.core.util.desktop.DesktopUI;
import pl.matsuo.core.util.desktop.DesktopUIData;
import pl.matsuo.core.util.desktop.component.ViewComponents;
import pl.matsuo.core.util.desktop.mvc.IActionController;
import pl.matsuo.core.util.desktop.mvc.IRequest;
import pl.matsuo.core.util.desktop.mvc.IView;
import pl.matsuo.core.util.desktop.showcase.view.BasicMenuView;
import pl.matsuo.core.util.desktop.showcase.view.NotFoundView;
import pl.matsuo.core.util.desktop.showcase.view.SampleView;

public class ShowcaseApplication extends DesktopUI {

  public ShowcaseApplication() {
    super(new DesktopUIData(views(), controllers(), null));
  }

  private static Map<String, IView<IRequest, Object>> views() {
    Map<String, IView<IRequest, Object>> views = new HashMap<>();

    ViewComponents viewComponents = new ViewComponents();

    views.put("/", new SampleView(viewComponents));
    views.put("/menu", new BasicMenuView(viewComponents));
    views.put("/404", new NotFoundView(viewComponents));

    return views;
  }

  private static Map<String, IActionController<IRequest, Object>> controllers() {
    Map<String, IActionController<IRequest, Object>> controllers = new HashMap<>();

    return controllers;
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}
