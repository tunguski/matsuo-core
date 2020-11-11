package pl.matsuo.core.util.desktop;

import j2html.tags.ContainerTag;

public interface IView<R extends IRequest, M> {

  ContainerTag view(R request, M model);
}
