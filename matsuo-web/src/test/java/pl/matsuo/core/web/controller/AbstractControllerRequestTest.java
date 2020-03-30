package pl.matsuo.core.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;
import pl.matsuo.core.web.mvc.MvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, FacadeBuilder.class, TestSessionState.class})
public abstract class AbstractControllerRequestTest {

  @Autowired protected WebApplicationContext wac;
  @Autowired protected FacadeBuilder facadeBuilder;
  @Autowired protected SessionState sessionState;

  protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;

  protected MockHttpServletRequestBuilder post(String url, Object content)
      throws JsonProcessingException {
    return MockMvcRequestBuilders.post(url)
        .content(objectMapper.writeValueAsString(content))
        .contentType(APPLICATION_JSON);
  }

  protected MockHttpServletRequestBuilder post(String url) throws JsonProcessingException {
    return MockMvcRequestBuilders.post(url);
  }

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  protected void performAndCheck(MockHttpServletRequestBuilder request, Consumer<String>... checks)
      throws Exception {
    performAndCheckStatus(request, status().isOk(), checks);
  }

  protected void performAndCheckStatus(
      MockHttpServletRequestBuilder request, ResultMatcher status, Consumer<String>... checks)
      throws Exception {
    ResultActions result = mockMvc.perform(request);
    result.andExpect(status);
    String html = result.andReturn().getResponse().getContentAsString();

    for (Consumer<String> check : checks) {
      check.accept(html);
    }
  }
}
