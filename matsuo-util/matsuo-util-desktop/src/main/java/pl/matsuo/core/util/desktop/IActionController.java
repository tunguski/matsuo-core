package pl.matsuo.core.util.desktop;

public interface IActionController<Q extends IRequest> {

  String execute(Q queryData);
}
