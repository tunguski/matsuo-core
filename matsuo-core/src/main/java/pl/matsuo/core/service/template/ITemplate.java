package pl.matsuo.core.service.template;

public interface ITemplate<E> {

  String generate(E model);

  String defaultName();
}
