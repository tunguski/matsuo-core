package pl.matsuo.core.service.facade;

import pl.matsuo.core.model.print.IPrintFacade;


/**
 * Created by tunguski on 08.12.13.
 */
public interface IFacadeAware {


  Class<? extends IPrintFacade> getPrintFacadeClass();
}

