package pl.matsuo.core.util.desktop;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class DesktopUIData {

  public Map<String, IView<IRequest>> views;
  public Map<String, IActionController<IRequest>> controllers;
}
