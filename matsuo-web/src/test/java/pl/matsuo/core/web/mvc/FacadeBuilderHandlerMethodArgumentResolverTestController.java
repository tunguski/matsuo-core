package pl.matsuo.core.web.mvc;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.IRequestParams;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


/**
 * Created by tunguski on 23.11.13.
 */
@RestController
@RequestMapping("/facadeBuilderHandlerMethodArgumentResolverTestController")
public class FacadeBuilderHandlerMethodArgumentResolverTestController {


  public static interface TestFacadeBuilderHandlerMethodArgumentResolverControllerParams extends IRequestParams {
    Integer getIdTest();

    List<String> getStringList();

    List<String> getOneElementList();

    String getNullValue();
  }


  @RequestMapping(value = "requestParam", method = GET)
  @ResponseStatus(I_AM_A_TEAPOT)
  public void requestParam(@RequestParam TestFacadeBuilderHandlerMethodArgumentResolverControllerParams params) {
    assertNotNull(params);
    assertEquals((Object) 777, params.getIdTest());
    assertEquals(1, params.getOneElementList().size());
    assertEquals(2, params.getStringList().size());
    assertEquals("a", params.getStringList().get(0));
    assertEquals("b", params.getStringList().get(1));
    // no value in request - checks that no NPE will be thrown
    assertNull(params.getNullValue());
  }


  @RequestMapping(value = "requestBody", method = GET)
  @ResponseStatus(I_AM_A_TEAPOT)
  public void requestBody(@RequestBody TestFacadeBuilderHandlerMethodArgumentResolverControllerParams params) {
    assertNotNull(params);
    assertEquals((Object) 777, params.getIdTest());
    assertEquals(1, params.getOneElementList().size());
    assertEquals(2, params.getStringList().size());
    assertEquals("a", params.getStringList().get(0));
    assertEquals("b", params.getStringList().get(1));
    // no value in request - checks that no NPE will be thrown
    assertNull(params.getNullValue());
  }


  @RequestMapping(value = "parameterProvider", method = GET)
  @ResponseStatus(I_AM_A_TEAPOT)
  public void parameterProvider(IParameterProvider<?> params) {
    assertNotNull(params);
    assertEquals((Object) 777, params.get("idTest", Integer.class));
  }
}

