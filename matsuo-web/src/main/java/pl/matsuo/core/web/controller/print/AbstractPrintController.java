package pl.matsuo.core.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.Initializer;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.service.numeration.NumerationService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.web.controller.AbstractSimpleController;

import java.util.List;

import static java.util.Arrays.*;


public abstract class AbstractPrintController<F extends IPrintFacade, P extends KeyValuePrint>
    extends AbstractSimpleController<P> {


  @Autowired
  protected NumerationService numerationService;
  @Autowired
  protected SessionState sessionState;


  @SuppressWarnings("unchecked")
  protected Class<F> printType = resolvePrintType(AbstractPrintController.class, 0);


  @Override
  protected List<? extends Initializer<? super P>> entityInitializers() {
    return asList(new PrintInitializer());
  }
}

