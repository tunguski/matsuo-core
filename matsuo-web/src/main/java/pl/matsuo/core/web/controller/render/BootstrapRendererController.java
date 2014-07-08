package pl.matsuo.core.web.controller.render;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.util.StringUtil.*;
import static pl.matsuo.core.web.view.BootstrapRenderer.*;


/**
 * Created by marek on 09.06.14.
 */
@RestController
@Transactional
@RequestMapping("/bootstrapRenderer")
public class BootstrapRendererController {


  /**
   * Pobiera pojedynczą encję danego typu po id.
   */
  @RequestMapping(method = GET)
  public String renderedField(IBootstrapRendererRequestParams requestParams,
                              IParameterProvider<Map<String, List<String>>> params) throws ClassNotFoundException {
    if (requestParams.getSingleField() != null) {
      return renderer().renderSingleField(
          Class.forName(requestParams.getEntityClass()), requestParams.getFieldName(), requestParams.getCssClasses());
    } else {
      BootstrapRenderingBuilder builder = renderer().create(Class.forName(requestParams.getEntityClass()));
      if (requestParams.getInline() != null) {
        builder = builder.inline(true);
      }
      if (requestParams.getEntityName() != null) {
        builder = builder.entityName(requestParams.getEntityName());
      }
      if (requestParams.getCssClasses() != null) {
        builder = builder.cssClasses(requestParams.getCssClasses().split(" "));
      }

      Map<String, List<String>> rawParams = params.getUnderlyingEntity();
      for (String key : rawParams.keySet()) {
        if (key.startsWith("mtf")) {
          String attrName = camelCaseToCssName(key.substring(3));
          builder = builder.attribute(attrName, rawParams.get(key).get(0));
        }
      }

      return requestParams.getHtmlName() != null ?
          builder.renderWithName(requestParams.getFieldName(), requestParams.getHtmlName())
          : builder.render(requestParams.getFieldName());
    }
  }
}

