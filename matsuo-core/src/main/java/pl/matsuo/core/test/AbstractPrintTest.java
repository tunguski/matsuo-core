package pl.matsuo.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;


public abstract class AbstractPrintTest<E extends IPrintFacade> extends AbstractPrintGeneratingTest<E> {


  @Autowired
  protected AbstractPrintService<E> printService;


  @Override
  protected Map<String, Object> buildModel(E print) {
    return printService.buildModel(print);
  }
}

