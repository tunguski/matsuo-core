package pl.matsuo.core.web.controller.print;

import static j2html.TagCreator.div;

import java.util.Map;
import pl.matsuo.core.service.template.ITemplate;

public class SampleTemplate implements ITemplate<Map<String, Object>> {

  @Override
  public String generate(Map<String, Object> model) {
    return div("test").render();
  }

  @Override
  public String defaultName() {
    return "sampleTemplate";
  }
}
