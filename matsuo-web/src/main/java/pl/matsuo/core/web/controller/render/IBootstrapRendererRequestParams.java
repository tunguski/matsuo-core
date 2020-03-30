package pl.matsuo.core.web.controller.render;

import pl.matsuo.core.params.IRequestParams;

public interface IBootstrapRendererRequestParams extends IRequestParams {
  String getEntityClass();

  String getEntityName();

  String getInline();

  String getSingleField();

  String getCssClasses();

  String getFieldName();

  String getHtmlName();
}
