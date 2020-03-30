package pl.matsuo.core.web.mvc;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.report.DataModelBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    classes = {
      MvcConfig.class,
      FacadeBuilder.class,
      FacadeBuilderHandlerMethodArgumentResolverTestController.class
    })
public class TestFacadeBuilderHandlerMethodArgumentResolver {

  private MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired WebApplicationContext wac;

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  @Test
  public void testRequestParam() throws Exception {
    mockMvc
        .perform(
            get("/facadeBuilderHandlerMethodArgumentResolverTestController/requestParam")
                .param("idTest", "777")
                .param("oneElementList", "1")
                .param("stringList", "a")
                .param("stringList", "b"))
        .andExpect(status().isIAmATeapot());
  }

  @Test
  public void testRequestBody() throws Exception {
    Map<String, Object> dataModel =
        new DataModelBuilder()
            .put("idTest", "777")
            .put("oneElementList", asList("1"))
            .put("stringList", asList("a", "b"))
            .getDataModel();

    mockMvc
        .perform(
            get("/facadeBuilderHandlerMethodArgumentResolverTestController/requestBody")
                .content(objectMapper.writeValueAsString(dataModel)))
        .andExpect(status().isIAmATeapot());
  }

  @Test
  public void testParameterProvider() throws Exception {
    mockMvc
        .perform(
            get("/facadeBuilderHandlerMethodArgumentResolverTestController/parameterProvider")
                .param("idTest", "777"))
        .andExpect(status().isIAmATeapot());
  }
}
