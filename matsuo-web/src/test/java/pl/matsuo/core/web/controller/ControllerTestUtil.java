package pl.matsuo.core.web.controller;

import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.ResultActions;
import pl.matsuo.core.service.facade.FacadeBuilder;

import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;


/**
 * Created by tunguski on 14.09.13.
 */
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


  public static <E> E queryFacade(Class<E> clazz, String ... keyValues) {
    return facadeBuilder.createFacade(stringMap(keyValues), clazz);
  }
}

