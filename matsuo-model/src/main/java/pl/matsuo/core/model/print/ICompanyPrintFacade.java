package pl.matsuo.core.model.print;

public interface ICompanyPrintFacade<E extends IPrintElementFacade> extends IPrintFacade<E> {

  PrintParty getCompany();
}
