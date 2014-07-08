package pl.matsuo.core.service.print;

import pl.matsuo.core.model.print.IPrintFacade;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by tunguski on 03.02.14.
 */
public abstract class AbstractPrintService<E extends IPrintFacade> {


  public final Map<String, Object> buildModel(E print) {
    Map<String, Object> dataModel = new HashMap<>();
    buildModel(print, dataModel);
    dataModel.put("print", print);
    return dataModel;
  }


  protected abstract void buildModel(E print, Map<String, Object> dataModel);


  public abstract String getFileName(E print);
}

