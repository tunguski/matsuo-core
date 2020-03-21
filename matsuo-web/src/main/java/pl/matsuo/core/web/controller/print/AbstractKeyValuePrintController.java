package pl.matsuo.core.web.controller.print;

import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.params.IQueryRequestParams;

public abstract class AbstractKeyValuePrintController<F extends IPrintFacade>
    extends AbstractPrintController<F, KeyValuePrint, IQueryRequestParams> {}
