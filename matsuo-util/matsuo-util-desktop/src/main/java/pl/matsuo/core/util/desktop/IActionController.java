package pl.matsuo.core.util.desktop;

public interface IActionController<Q extends IRequest, M> {

  IRequest execute(Q queryData, M model);
}
