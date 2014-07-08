package pl.matsuo.core.web.controller.render;

import pl.matsuo.core.IRequestParams;

/**
 * Created by marek on 09.06.14.
 */
public interface IBootstrapRendererRequestParams extends IRequestParams {
  String getEntityClass();
  String getEntityName();
  String getInline();
  String getSingleField();
  String getCssClasses();
  String getFieldName();
  String getHtmlName();
}
