package pl.matsuo.core.util.desktop;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class DesktopUIData<M> {

  public Map<String, IView<IRequest, M>> views;
  public Map<String, IActionController<IRequest>> controllers;
  public M model;
}
