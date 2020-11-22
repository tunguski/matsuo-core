package pl.matsuo.core.util.desktop;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.matsuo.core.util.desktop.mvc.IActionController;
import pl.matsuo.core.util.desktop.mvc.IRequest;
import pl.matsuo.core.util.desktop.mvc.IView;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DesktopUIData<M> {

  public Map<String, IView<IRequest, M>> views;
  public Map<String, IActionController<IRequest, M>> controllers;
  public M model;
}
