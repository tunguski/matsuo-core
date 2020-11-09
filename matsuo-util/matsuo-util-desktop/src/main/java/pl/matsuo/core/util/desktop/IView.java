package pl.matsuo.core.util.desktop;

import j2html.tags.ContainerTag;

public interface IView<M extends IRequest> {

  ContainerTag view(M model);
}
