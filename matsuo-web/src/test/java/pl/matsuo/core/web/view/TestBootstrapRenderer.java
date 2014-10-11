package pl.matsuo.core.web.view;

import org.junit.Test;
import pl.matsuo.core.util.function.ThrowingExceptionsConsumer;

import java.util.Arrays;

import static org.junit.Assert.*;


public class TestBootstrapRenderer {


  protected BootstrapRenderer renderer = new BootstrapRenderer();


  public TestBootstrapRenderer() {
    renderer.init();
  }


  private void checkConstraints(String rendered, ThrowingExceptionsConsumer<String> ... validations) throws Exception {
    assertNotNull(rendered);

    for (ThrowingExceptionsConsumer<String> validation : validations) {
      validation.accept(rendered);
    }
  }


  private void checkConstraints(Class<?> model, String field,
                                ThrowingExceptionsConsumer<String> ... validations) throws Exception {
    checkConstraints(renderer.create(model).render(field), validations);
  }


  protected void assertContains(String rendered, String subPart) {
    assertTrue("fragment:\n" + subPart + "\nnot found in rendered:\n" + rendered, rendered.contains(subPart));
  }


  protected ThrowingExceptionsConsumer<String> containsAssertions(String ... parts) {
    return rendered -> Arrays.asList(parts).forEach(part -> assertContains(rendered, part));
  }


  @Test
  public void testRendering() throws Exception {
    checkConstraints(renderer.create(TestModel.class).render("date", "time", "enumValue"));
    checkConstraints(renderer.renderSingleField(TestModel.class, "testType"));
    checkConstraints(renderer.create(TestModel.class).renderWithName("enumValue", "htmlFieldName"));
  }


  @Test
  public void testPasswordRendering() throws Exception {
    checkConstraints(TestModel.class, "password", containsAssertions("type=\"password\""));
  }


  @Test
  public void testBooleanRendering() throws Exception {
    checkConstraints(TestModel.class, "bool", containsAssertions("type=\"checkbox\"", "class=\" col-sm-6 checkbox\""));
  }


  @Test
  public void testRenderingBasedOnInterface() throws Exception {
    checkConstraints(renderer.create(ITestModel.class).render("date", "time", "enumValue"),
        containsAssertions("<option "));
  }


  @Test
  public void testDateRendering() throws Exception {
    checkConstraints(TestModel.class, "date", containsAssertions("input", "type=\"text\"", "mt-datepicker=\"datepickerOptions\""));
  }


  @Test
  public void testStringRendering() throws Exception {
    checkConstraints(TestModel.class, "text", containsAssertions("input", "type=\"text\""));
  }


  @Test
  public void testIntegerRendering() throws Exception {
    checkConstraints(TestModel.class, "duration", rendered -> {
      containsAssertions("input", "type=\"text\"", "ng-pattern=\"/^([0-9]+([.,][0-9]+)?)?$/\"").accept(rendered);

      assertFalse(rendered.contains("ui-select"));
    });
  }


  @Test
  public void testIdRendering() throws Exception {
    checkConstraints(TestModel.class, "subModel.id", containsAssertions("ui-select"));
  }


  @Test
  public void testPatternOnAnnotationRendering() throws Exception {
    checkConstraints(TestModel.class, "duration",
        containsAssertions("input", "type=\"text\"", "ng-pattern=\"/^([0-9]+([.,][0-9]+)?)?$/\""));
  }


  @Test
  public void testGenereSelectForReferenceId() throws Exception {
    checkConstraints(TestModel.class, "reference", containsAssertions(
        "ui-select", "ng-model=\"reference.value\"", "ng-disabled=\"reference.options.disabled\""));
  }


  @Test
  public void testGenereSelectForReference() throws Exception {
    checkConstraints(TestModel.class, "subModel", containsAssertions(
        "ui-select", "ng-model=\"entity.subModel\"", "ng-disabled=\"subModel.options.disabled\""));
  }


  @Test
  public void testRenderingWithCustomName() throws Exception {
    checkConstraints(renderer.create(TestModel.class).entityName("customEntityName").render("date"),
        containsAssertions("ng-model=\"customEntityName.date\""));
  }


  @Test
  public void testRenderingInlineElements() throws Exception {
    checkConstraints(renderer.create(TestModel.class).entityName("customEntityName")
        .attribute("ng-test", "ng-value").render("date", "date"), rendered -> {
      assertContains(rendered, "ng-model=\"customEntityName.date\"");
      // dokładnie dwa wystąpienia
      assertTrue(rendered.split("ng-test=\"ng-value\"").length == 3);
    });
  }


  @Test
  public void testRenderingAdditionalAttribute() throws Exception {
    checkConstraints(renderer.create(TestModel.class)
        .attribute("test-attr", "test-value")
        .attribute("id", "id-value")
        .render("subModel.date"), rendered -> {
      assertContains(rendered, "test-attr=\"test-value\"");
      assertContains(rendered, "id=\"id-value\"");
      assertFalse(rendered.contains("id=\"entity.subModel.date\""));
    });
  }
}

