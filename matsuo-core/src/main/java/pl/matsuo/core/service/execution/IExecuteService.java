package pl.matsuo.core.service.execution;



/**
 * Interfejs dla serwisów, które mają zostać wykonane.
 * @author Marek Romanowski
 * @since 11-07-2013
 */
public interface IExecuteService {


  String getExecuteServiceName();


  void execute();
}
