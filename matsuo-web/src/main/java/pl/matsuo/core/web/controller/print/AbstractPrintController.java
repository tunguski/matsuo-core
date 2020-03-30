package pl.matsuo.core.web.controller.print;

import static java.util.Arrays.asList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.service.numeration.NumerationService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.web.controller.AbstractController;

public abstract class AbstractPrintController<
        F extends IPrintFacade, E extends KeyValuePrint, P extends IQueryRequestParams>
    extends AbstractController<E, P> {

  @Autowired protected NumerationService numerationService;
  @Autowired protected SessionState sessionState;

  @Override
  protected List<? extends Initializer<? super E>> entityInitializers() {
    return asList(new PrintInitializer());
  }
}
