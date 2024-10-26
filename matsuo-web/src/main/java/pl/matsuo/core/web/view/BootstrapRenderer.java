package pl.matsuo.core.web.view;

import static j2html.TagCreator.div;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.option;
import static j2html.TagCreator.select;
import static j2html.TagCreator.span;
import static j2html.TagCreator.text;
import static java.beans.Introspector.decapitalize;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.springframework.util.StringUtils.capitalize;
import static pl.matsuo.core.util.ArrayUtil.last;
import static pl.matsuo.core.util.ReflectUtil.getAnnotatedElement;
import static pl.matsuo.core.util.ReflectUtil.getPropertyType;

import com.google.common.base.Joiner;
import j2html.TagCreator;
import j2html.attributes.Attr;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.model.validation.PasswordField;

/** Rendering bootstrap pages. */
@Slf4j
@Component
public class BootstrapRenderer {

  private static BootstrapRenderer bootstrapRenderer;

  Validator validator;

  @PostConstruct
  public void init() {
    if (bootstrapRenderer != null) {
      log.error("Second spring bean BootstrapRenderer");
    }

    bootstrapRenderer = this;
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  protected String renderField(
      Class<?> fieldType,
      AnnotatedElement annotatedElement,
      String fieldName,
      BootstrapRenderingBuilder builder) {
    String fullFieldName = fullFieldName(builder.entityName, fieldName);

    return createControlGroup(
        fullFieldName,
        createControls(
            fieldType,
            annotatedElement,
            fieldName,
            builder.entityType,
            fullFieldName,
            builder,
            // css classes
            addAll(builder.cssClasses, lastNameElement(fieldName.split("[.]")))));
  }

  private String createControlGroup(String fullFieldName, ContainerTag controls) {
    return div(
            attrs(
                asList(
                        "form-group",
                        fullFieldName.replaceAll("[.-]", "_"),
                        bindServerErrorPath(fullFieldName, " && 'error' || ''"))
                    .toString()),
            label(attrs(".col-sm-4.control-label"))
                .attr("for", fullFieldName)
                .attr("translate", fullFieldName),
            controls)
        .renderFormatted();
  }

  private String serverErrorPath(String fullFieldName) {
    return formField(fullFieldName, "serverError");
  }

  private String bindServerErrorPath(String fullFieldName, String suffix) {
    return "{{" + formField(fullFieldName, "serverError") + suffix + "}}";
  }

  private String formField(String fullFieldName, String field) {
    return joinDot("form", fullFieldName.replaceAll("[.-]", "_"), field);
  }

  private String joinDot(String... parts) {
    return Joiner.on(".").join(parts);
  }

  private Attr.ShortForm attrs(String... parts) {
    return TagCreator.attrs("." + Joiner.on(".").join(parts));
  }

  /** Tworzy pole formularza razem z helpem, opisem itp. */
  private ContainerTag createControls(
      Class<?> fieldType,
      AnnotatedElement annotatedElement,
      String fieldName,
      Class<?> entityType,
      String fullFieldName,
      BootstrapRenderingBuilder builder,
      String... cssClasses) {
    return div(
        attrs("col-sm-6", isCheckbox(fieldType) ? "checkbox" : ""),
        createInput(
            fieldType, annotatedElement, fullFieldName, entityType, fieldName, builder, cssClasses),
        span(
            attrs("help-inline", bindServerErrorPath(fullFieldName, " ? '' : 'hide'")),
            text(bindServerErrorPath(fullFieldName, ""))));
  }

  private String lastNameElement(String[] splitted) {
    String lastNameElement = splitted[splitted.length - 1];
    if (lastNameElement.equals("id")) {
      lastNameElement = lastNameElement + capitalize(splitted[splitted.length - 2]);
    }

    return lastNameElement;
  }

  private boolean isCheckbox(Class<?> fieldType) {
    return boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType);
  }

  protected ContainerTag createSelect(String lastNameElement, String constantValues) {
    ContainerTag element = select(option(text(lastNameElement)), option(text(constantValues)));
    //        el(
    //                "ui-select",
    //                asList(""),
    //                el("ui-select-match", asList(""), text("{{ formatElement($select.selected)
    // }}"))
    //                    .attr("placeholder", "{{ options.placeholderText | translate }}"),
    //                el(
    //                        "ui-select-choices",
    //                        asList(""),
    //                        div().attr("ng-bind-html", "formatElement(item)"))
    //                    .attr("repeat", "item in " + constantValues + " | filter: $select.search")
    //                    .attr("refresh", "searchElements($select.search)"))
    //            .attr("mt-select-options", joinDot(lastNameElement, "options"))
    //            .attr("ng-disabled", joinDot(lastNameElement, "options.disabled"));
    return element;
  }

  /** Tworzy kontrolkę formularza. */
  private Tag createInput(
      Class<?> fieldType,
      AnnotatedElement annotatedElement,
      String fullFieldName,
      Class<?> entityType,
      String fieldName,
      BootstrapRenderingBuilder builder,
      String... cssClasses) {
    boolean addFormControlStyle = true;
    Tag el;
    Tag inputIfNotEl = null;
    String ngModel = fullFieldName;

    if (Enum.class.isAssignableFrom(fieldType)) {
      el =
          select(
              getEnumValuesElements(
                  (Class<? extends Enum<?>>) fieldType,
                  !isAnnotationPresent(annotatedElement, NotNull.class)));
    } else if (Time.class.isAssignableFrom(fieldType)) {
      el =
          input(attrs(".input-size-time.timepicker"))
              .attr("type", "text")
              .attr("placeholder", "HH:mm");
      pattern(el, "[0-2][0-9]:[0-5][0-9]");
    } else if (Date.class.isAssignableFrom(fieldType)) {
      el =
          input(attrs(".input-size-date"))
              .attr("type", "text")
              .attr("mt-datepicker", "datepickerOptions");
    } else if (isAnnotationPresent(
        annotatedElement, ManyToOne.class, EntityReference.class, OneToOne.class)) {
      String[] splitted = fullFieldName.split("[.]");
      String lastNameElement = lastNameElement(splitted);

      el = createSelect(lastNameElement, "$select.elements");

      if (fieldType.equals(Integer.class)) {
        ngModel = joinDot(lastNameElement, "value");
      }
      addFormControlStyle = false;
    } else if (isCheckbox(fieldType)) {
      inputIfNotEl = input().attr("type", "checkbox");
      el = label(inputIfNotEl, text("&nbsp;"));

      addFormControlStyle = false;
    } else {
      el = input().attr("type", "text");

      if (Number.class.isAssignableFrom(fieldType)) {
        pattern(el, "[0-9]+([.,][0-9]+)?");
      }

      if (isAnnotationPresent(annotatedElement, PasswordField.class)) {
        el.attr("type", "password");
      }
    }

    addFieldValidation(fieldType, entityType, el, fieldName);
    Tag val = inputIfNotEl != null ? inputIfNotEl : el;

    val.attr("id", fullFieldName)
        .attr("name", fullFieldName.replaceAll("\\.", "_"))
        .attr("ng-model", ngModel)
        .attr("placeholder", "{{ '" + fullFieldName + "' | translate }}")
    // .withClasses(cssClasses)
    //        .style(cssClasses)
    ;

    if (addFormControlStyle) {
      //      val.style("form-control");
    }

    if (builder != null) {
      for (String attr : builder.attributes.keySet()) {
        val.attr(attr, builder.attributes.get(attr));
      }
    }

    return el;
  }

  private boolean isAnnotationPresent(
      AnnotatedElement annotatedElement, Class<? extends Annotation>... annotations) {
    if (annotatedElement != null) {
      for (Class<? extends Annotation> annotation : annotations) {
        if (annotatedElement.isAnnotationPresent(annotation)) {
          return true;
        }
      }
    }

    return false;
  }

  private void addFieldValidation(
      Class<?> fieldType, Class<?> entityType, Tag el, String fieldName) {
    if (entityType == null) {
      return;
    }

    String propertyName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
    if (fieldName.contains(".")) {
      entityType = getPropertyType(entityType, fieldName.substring(0, fieldName.lastIndexOf(".")));
    }

    BeanDescriptor constraintsForClass = validator.getConstraintsForClass(entityType);
    jakarta.validation.metadata.PropertyDescriptor constraintsForProperty =
        constraintsForClass.getConstraintsForProperty(propertyName);

    if (constraintsForProperty != null) {
      Set<ConstraintDescriptor<?>> constraintDescriptors =
          constraintsForProperty.getConstraintDescriptors();

      for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
        Object annotation = constraintDescriptor.getAnnotation();

        if (NotNull.class.isAssignableFrom(annotation.getClass())) {
          el.attr("required", null);
        } else if (Pattern.class.isAssignableFrom(annotation.getClass())) {
          Pattern pattern = (Pattern) annotation;
          pattern(el, pattern.regexp());
        } else if (Digits.class.isAssignableFrom(annotation.getClass())) {
          Digits digits = (Digits) annotation;
          pattern(el, "[0-9]{" + digits.integer() + "}([.,][0-9]{" + digits.fraction() + "})?");
        } else if (isAnnotatedAnnotation(annotation.getClass(), Pattern.class) != null) {
          Pattern pattern = isAnnotatedAnnotation(annotation.getClass(), Pattern.class);
          pattern(el, pattern.regexp());
        }
      }
    }
  }

  private <A extends Annotation> A isAnnotatedAnnotation(Class<?> clazz, Class<A> annotation) {
    if (clazz.getAnnotation(annotation) != null) {
      return annotation.getClass().getAnnotation(annotation);
    } else {
      for (Class<?> iface : clazz.getInterfaces()) {
        if (iface.getAnnotation(annotation) != null) {
          return iface.getAnnotation(annotation);
        }
      }
    }

    return null;
  }

  private void pattern(Tag el, String pattern) {
    el.attr("ng-pattern", "/^(" + pattern + ")?$/");
  }

  private ContainerTag[] getEnumValuesElements(
      Class<? extends Enum<?>> propertyType, boolean withEmptyElement) {
    List<ContainerTag> elements = new ArrayList<>();

    if (withEmptyElement) {
      elements.add(option());
    }

    for (Enum<?> enumElement : propertyType.getEnumConstants()) {
      elements.add(
          option()
              .attr(
                  "translate",
                  joinDot("enum", propertyType.getSimpleName(), enumElement.toString()))
              .attr("value", enumElement.name()));
    }

    return elements.toArray(new ContainerTag[0]);
  }

  public BootstrapRenderingBuilder create(Class<?> entityType) {
    return new BootstrapRenderingBuilder(entityType);
  }

  /** Klasa buildera pozwalająca na zdefiniowanie parametrów renderowania pól. */
  public static class BootstrapRenderingBuilder {
    private Class<?> entityType;
    private String entityName = "entity";
    private boolean inline = false;
    private String[] cssClasses;
    private Map<String, String> attributes = new HashMap<>();

    public BootstrapRenderingBuilder(Class<?> entityType) {
      this.entityType = entityType;
    }

    public BootstrapRenderingBuilder entityName(String entityName) {
      this.entityName = entityName;
      return this;
    }

    public BootstrapRenderingBuilder inline(boolean inline) {
      this.inline = inline;
      return this;
    }

    public BootstrapRenderingBuilder cssClasses(String... cssClasses) {
      this.cssClasses = cssClasses;
      return this;
    }

    public BootstrapRenderingBuilder attribute(String name, String value) {
      attributes.put(name, value);
      return this;
    }

    public String render(String... fields) {
      if (inline) {
        return renderer().renderInlineFields(fields, this);
      } else {
        return asList(fields).stream()
            .reduce(
                "",
                (sum, fieldName) -> {
                  return sum
                      + renderer()
                          .renderField(
                              getPropertyType(entityType, fieldName),
                              getAnnotatedElement(entityType, fieldName),
                              fieldName,
                              this)
                      + "\n";
                });
      }
    }

    public String renderWithName(String entityFieldName, String htmlFieldName) {
      return renderer()
          .renderField(
              getPropertyType(entityType, entityFieldName),
              getAnnotatedElement(entityType, entityFieldName),
              htmlFieldName,
              this);
    }
  }

  /**
   * Renderuje listę pól należących do encji <code>entityType</code>. Prefiksem nazw będzie "entity"
   * - jako generyczne odwowłanie do w/w obiektu.
   */
  private String renderInlineFields(String[] fields, BootstrapRenderingBuilder builder) {
    List<Tag> elements = new ArrayList<>();

    for (String fieldName : fields) {
      String fullFieldName = fullFieldName(builder.entityName, fieldName);
      String simpleElementName = last(fieldName.split("[.]"));

      elements.add(
          span(attrs(".inline-form-text." + simpleElementName)).attr("translate", fullFieldName));

      elements.add(
          createInput(
              getPropertyType(builder.entityType, fieldName),
              getAnnotatedElement(builder.entityType, fieldName),
              fullFieldName,
              builder.entityType,
              "entity",
              builder,
              // css classes
              simpleElementName));
    }
    elements.remove(0);

    elements.add(span(attrs(".help-inline"), text(bindServerErrorPath(builder.entityName, ""))));

    return createControlGroup(
        fullFieldName(builder.entityName, fields[0]),
        div(attrs(".controls"), elements.toArray(new Tag[0])));
  }

  /**
   * Renderuje pojedyncze pole typu <code>fieldType</code>, o nazwie <code>fieldName</code>, któremu
   * opcjonalnie można przypisać dodatkowe klasy stylu <code>cssClasses</code>.
   */
  public String renderSingleField(Class<?> fieldType, String fieldName, String... cssClasses) {
    return renderField(fieldType, null, fieldName, create(null).cssClasses(cssClasses)) + "\n";
  }

  public String renderSingleField(Method method, String fieldName, String... cssClasses) {
    return renderField(
            method.getReturnType(), method, fieldName, create(null).cssClasses(cssClasses))
        + "\n";
  }

  private String fullFieldName(String entityName, String fieldName) {
    return (entityName != null ? decapitalize(entityName) + "." : "") + fieldName;
  }

  public static BootstrapRenderer renderer() {
    return bootstrapRenderer;
  }
}
