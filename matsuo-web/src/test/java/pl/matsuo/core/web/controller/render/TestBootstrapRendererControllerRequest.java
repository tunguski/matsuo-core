package pl.matsuo.core.web.controller.render;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;
import pl.matsuo.core.web.controller.AbstractControllerRequestTest;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = { BootstrapRendererController.class })
public class TestBootstrapRendererControllerRequest extends AbstractControllerRequestTest {
  private static final Logger logger = LoggerFactory.getLogger(TestBootstrapRendererControllerRequest.class);


  @Test
  public void testBasicEntityField() throws Exception {
    // entityClass=Person&fieldName=firstName
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("fieldName", "firstName"),
        html -> assertTrue(html.contains("<input")));
  }


  @Test
  public void testCustomEntityName() throws Exception {
    // entityClass=Tool&entityName=person&fieldName=name
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("entityName", "person")
            .param("fieldName", "firstName"),
        html -> assertTrue(html.contains("<input")));
  }


  @Test
  public void testCustomHtmlFieldName() throws Exception {
    // entityClass=OrganizationUnit&fieldName=idOwner&htmlName=owner.id
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("fieldName", "firstName")
            .param("htmlName", "formBaseName"),
        html -> assertTrue(html.contains("<input")));
  }


  /**
   * FIXME! It should work.
   */
  @Test(expected = NestedServletException.class)
  public void testCustomHtmlFieldName_toFix() throws Exception {
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("fieldName", "firstName")
            .param("htmlName", "form.baseName"),
        html -> assertTrue(html.contains("<input")));
  }


  @Test
  public void testWithAttributes() throws Exception {
    // entityClass=Person&fieldName=firstName&mtfNgDisabled=true&mtfCustomAttr=xxxy
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("fieldName", "firstName")
            .param("mtfNgDisabled", "true")
            .param("mtfCustomAttr", "xxxy"),
        html -> assertTrue(html.contains("<input")));
  }


  @Test
  public void testInline() throws Exception {
    // entityClass=Person&inline=true&fieldName=firstName
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("inline", "true")
            .param("fieldName", "firstName"),
        html -> assertTrue(html.contains("<input")));
  }


  @Test
  public void testSingleField() throws Exception {
    // entityClass=Person&singleField=true&fieldName=firstName
    performAndCheck(get("/bootstrapRenderer")
            .param("entityClass", "pl.matsuo.core.model.organization.Person")
            .param("singleField", "true")
            .param("fieldName", "firstName"),
        html -> assertTrue(html.contains("<input")));
  }
}

