package pl.matsuo.core.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/lists")
public class GenericListsController {


  protected Map<String, Class<? extends Enum<?>>> enums = new HashMap<>();


  public void registerEnum(Class<? extends Enum<?>>... enumClasses) {
    for (Class<? extends Enum<?>> enumClass : enumClasses) {
      enums.put(uncapitalize(enumClass.getSimpleName()), enumClass);
    }
  }


  @RequestMapping(value = "/{id}", method = GET)
  public List<String> list(@PathVariable("id") String id) {
    List<String> elements = new ArrayList<>();

    Class<? extends Enum<?>> clazz = enums.get(uncapitalize(id));
    if (clazz != null) {
      for (Enum value : clazz.getEnumConstants()) {
        elements.add(value.name());
      }
    }

    return elements;
  }
}

