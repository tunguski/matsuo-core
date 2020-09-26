package pl.matsuo.core.service.print;

import static pl.matsuo.core.util.collection.CollectionUtil.last;

import java.util.function.Consumer;
import pl.matsuo.core.model.print.IPrintElementFacade;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.service.facade.IFacadeBuilder;

public interface PrintMethods {

  IFacadeBuilder getFacadeBuilder();

  default <E extends IPrintFacade<F>, F extends IPrintElementFacade> void addElements(
      KeyValuePrint print, E facade, Consumer<? super F>... elementInitializations) {
    for (Consumer<? super F> elementInitialization : elementInitializations) {
      print.getElements().add(new KeyValuePrintElement());
      elementInitialization.accept(last(facade.getElements()));
    }
  }

  default <E extends IPrintFacade<F>, F extends IPrintElementFacade> KeyValuePrint initializePrint(
      Class<E> printClass,
      Long idEntity,
      Consumer<E> initializePrint,
      Consumer<? super F>... elementInitializations) {
    KeyValuePrint print = KeyValuePrint.print(printClass, idEntity).get();
    getFacadeBuilder()
        .doWithFacade(
            print,
            printClass,
            facade -> {
              // first add all positions
              addElements(print, facade, elementInitializations);
              // then initialize print (may contain summary of positions)
              initializePrint.accept(facade);
            });
    return print;
  }

  default <E extends IPrintFacade<F>, F extends IPrintElementFacade> E initializeFacade(
      Class<E> printClass,
      Long idEntity,
      Consumer<E> initializePrint,
      Consumer<? super F>... elementInitializations) {
    return getFacadeBuilder()
        .createFacade(
            initializePrint(printClass, idEntity, initializePrint, elementInitializations),
            printClass);
  }

  default <E extends IPrintFacade<F>, F extends IPrintElementFacade, P extends KeyValuePrint>
      P initializePrint(
          P print,
          Class<E> printClass,
          Consumer<? super E> initializePrint,
          Consumer<? super F>... elementInitializations) {
    getFacadeBuilder()
        .doWithFacade(
            print,
            printClass,
            facade -> {
              // first add all positions
              addElements(print, facade, elementInitializations);
              // then initialize print (may contain summary of positions)
              initializePrint.accept(facade);
            });
    return print;
  }

  default <E extends IPrintFacade<F>, F extends IPrintElementFacade, P extends KeyValuePrint>
      E initializeFacade(
          P print,
          Class<E> printClass,
          Consumer<E> initializePrint,
          Consumer<? super F>... elementInitializations) {
    return getFacadeBuilder()
        .createFacade(initializePrint(print, printClass, initializePrint, elementInitializations));
  }
}
