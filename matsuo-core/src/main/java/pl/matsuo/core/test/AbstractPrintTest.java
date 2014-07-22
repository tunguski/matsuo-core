package pl.matsuo.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;

import static org.springframework.util.StringUtils.*;
import static pl.matsuo.core.util.ReflectUtil.resolveType;


public abstract class AbstractPrintTest<E extends IPrintFacade> extends AbstractPrintGeneratingTest<E> {


  @SuppressWarnings("unchecked")
  protected final Class<E> printType = resolveType(getClass(), AbstractPrintTest.class, 0);


  @Autowired
  protected AbstractPrintService<E> printService;


  @Override
  protected Map<String, Object> buildModel(E print) {
    return printService.buildModel(print);
  }


  @Override
  protected String getPrintFileName() {
    return "/print/" + uncapitalize(printType.getSimpleName()) + ".ftl";
  }
}

