package pl.matsuo.core.web.controller;

import static java.lang.Long.parseLong;
import static pl.matsuo.core.util.collection.CollectionUtil.stringMap;

import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.ResultActions;
import pl.matsuo.core.service.facade.FacadeBuilder;

public class ControllerTestUtil {

  public static FacadeBuilder facadeBuilder = new FacadeBuilder();

  public static Long idFromLocation(ResultActions result) {
    return idFromLocation(result.andReturn().getResponse().getHeader("Location"));
  }

  public static Long idFromLocation(HttpEntity httpEntity) {
    return idFromLocation(httpEntity.getHeaders().getLocation().toASCIIString());
  }

  public static Long idFromLocation(String location) {
    String[] split = location.split("/");
    return parseLong(split[split.length - 1]);
  }

  public static <E> E queryFacade(Class<E> clazz, String... keyValues) {
    return facadeBuilder.createFacade(stringMap(keyValues), clazz);
  }
}
