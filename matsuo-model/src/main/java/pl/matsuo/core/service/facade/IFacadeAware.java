package pl.matsuo.core.service.facade;

import pl.matsuo.core.model.print.IPrintFacade;

public interface IFacadeAware {

  Class<? extends IPrintFacade> getPrintFacadeClass();
}
