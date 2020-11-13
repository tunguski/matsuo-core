package pl.matsuo.core.util.desktop;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DesktopUIData<M> {

  public Map<String, IView<IRequest, M>> views;
  public Map<String, IActionController<IRequest, M>> controllers;
  public M model;
}
