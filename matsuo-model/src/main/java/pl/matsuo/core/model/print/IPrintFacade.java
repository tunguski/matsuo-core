package pl.matsuo.core.model.print;

import java.util.List;


public interface IPrintFacade<E extends IPrintElementFacade> extends IPrintElementFacade {

  List<E> getElements();
}

