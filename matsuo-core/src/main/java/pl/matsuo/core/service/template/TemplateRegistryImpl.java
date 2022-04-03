package pl.matsuo.core.service.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateRegistryImpl implements ITemplateRegistry {

  final Map<String, ITemplate> templates = new HashMap<>();

  public TemplateRegistryImpl(List<ITemplate> templates) {
    templates.forEach(template -> this.templates.put(template.defaultName(), template));
  }

  public ITemplate getTemplate(String name) {
    if (!templates.containsKey(name)) {
      log.warn("Template not defined: " + name);
    }
    return templates.get(name);
  }
}
