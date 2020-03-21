package pl.matsuo.core.model.print.initializer;

import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.KeyValuePrintElement;

public class PrintInitializer implements Initializer<KeyValuePrint> {

  @Override
  public void init(KeyValuePrint print) {
    if (print.getFields() != null) {
      print.getFields().size();
    }

    for (KeyValuePrintElement printElement : print.getElements()) {
      printElement.getFields().size();
    }
  }
}
