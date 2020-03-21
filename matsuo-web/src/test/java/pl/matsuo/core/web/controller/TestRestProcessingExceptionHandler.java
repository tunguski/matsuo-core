package pl.matsuo.core.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.web.controller.exception.RestProcessingExceptionHandler;

/** Created by tunguski on 20.09.13. */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    classes = {
      RestProcessingExceptionHandler.class,
      TestRestProcessingExceptionHandler.RpehTestConfiguration.class,
      TestRestProcessingExceptionHandler.RestProcessingExceptionThrowingController.class
    })
public class TestRestProcessingExceptionHandler {

  private MockMvc mockMvc;
  @Autowired WebApplicationContext wac;

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  @Configuration
  @EnableWebMvc
  public static class RpehTestConfiguration {}

  @RestController
  @RequestMapping("/tests")
  public static class RestProcessingExceptionThrowingController {

    /** Pobiera pojedynczą encję danego typu po id. */
    @RequestMapping(value = "/exception", method = GET)
    public String find() {
      throw new RestProcessingException("global_error_test");
    }
  }

  @Test
  public void testHandleException() throws Exception {
    mockMvc
        .perform(get("/tests/exception"))
        .andExpect(
            result -> result.getResponse().getContentAsString().contains("global_error_test"))
        .andExpect(status().isBadRequest());
  }
}
