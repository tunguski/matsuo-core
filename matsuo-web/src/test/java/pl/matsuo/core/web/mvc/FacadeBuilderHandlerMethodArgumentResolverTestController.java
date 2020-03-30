package pl.matsuo.core.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.params.IRequestParams;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;

@RestController
@RequestMapping("/facadeBuilderHandlerMethodArgumentResolverTestController")
public class FacadeBuilderHandlerMethodArgumentResolverTestController {

  public static interface TestFacadeBuilderHandlerMethodArgumentResolverControllerParams
      extends IRequestParams {
    Integer getIdTest();

    List<String> getStringList();

    List<String> getOneElementList();

    String getNullValue();
  }

  @RequestMapping(value = "requestParam", method = GET)
  @ResponseStatus(I_AM_A_TEAPOT)
  public void requestParam(
      @RequestParam TestFacadeBuilderHandlerMethodArgumentResolverControllerParams params) {
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
  public void requestBody(
      @RequestBody TestFacadeBuilderHandlerMethodArgumentResolverControllerParams params) {
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
