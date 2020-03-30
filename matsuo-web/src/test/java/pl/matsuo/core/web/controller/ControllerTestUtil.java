package pl.matsuo.core.web.controller;

import static pl.matsuo.core.util.NumberUtil.i;
import static pl.matsuo.core.util.collection.CollectionUtil.stringMap;

import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.ResultActions;
import pl.matsuo.core.service.facade.FacadeBuilder;

public class ControllerTestUtil {

  public static FacadeBuilder facadeBuilder = new FacadeBuilder();

  public static Integer idFromLocation(ResultActions result) {
    return idFromLocation(result.andReturn().getResponse().getHeader("Location"));
  }

  public static Integer idFromLocation(HttpEntity httpEntity) {
    return idFromLocation(httpEntity.getHeaders().getLocation().toASCIIString());
  }

  public static Integer idFromLocation(String location) {
    String[] split = location.split("/");
    return i(split[split.length - 1]);
  }

  public static <E> E queryFacade(Class<E> clazz, String... keyValues) {
    return facadeBuilder.createFacade(stringMap(keyValues), clazz);
  }
}
