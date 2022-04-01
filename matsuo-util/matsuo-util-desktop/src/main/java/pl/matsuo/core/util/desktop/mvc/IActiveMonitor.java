package pl.matsuo.core.util.desktop.mvc;

import org.w3c.dom.events.Event;

public interface IActiveMonitor<M> {

  /**
   * @return <code>true</code> if model was changed
   */
  boolean onChange(String inputName, Event ev, M model);
}
