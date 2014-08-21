package pl.matsuo.core.web.view;

import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import pl.matsuo.core.model.validation.EntityReference;

import javax.annotation.PostConstruct;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
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
import java.util.logging.Logger;

import static java.beans.Introspector.*;
import static java.util.Arrays.*;
import static org.apache.commons.lang3.ArrayUtils.*;
import static org.springframework.util.StringUtils.*;
import static pl.matsuo.core.util.ReflectUtil.*;
import static pl.matsuo.core.util.collection.ArrayUtil.*;


/**
 * Zbiór metod automatyzujących generowanie kodu jsp.
 * @author Marek Romanowski
 * @since 09-06-2013
 */
@Component
public class BootstrapRenderer {
  private static Logger logger = Logger.getLogger(BootstrapRenderer.class.getName());


  private static BootstrapRenderer bootstrapRenderer;


  Validator validator;


  @PostConstruct
  public void init() {
    if (bootstrapRenderer != null) {
      logger.severe("Second spring bean BootstrapRenderer");
    }

    bootstrapRenderer = this;

    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }


  protected String renderField(Class<?> fieldType, AnnotatedElement annotatedElement,
      String fieldName, BootstrapRenderingBuilder builder) {
    String fullFieldName = fullFieldName(builder.entityName, fieldName);

    return createControlGroup(fullFieldName, createControls(
        fieldType, annotatedElement, fieldName, builder.entityType, fullFieldName, builder,
        // css classes
        addAll(builder.cssClasses, lastNameElement(fieldName.split("[.]")))));
  }


  private String createControlGroup(String fullFieldName, HtmlElement controls) {
    return div(asList("form-group", fullFieldName.replaceAll("[.-]", "_"),
            bindServerErrorPath(fullFieldName, " && 'error' || ''")),
        el("label", asList("col-sm-4 control-label"))
              .attr("for", fullFieldName)
              .attr("translate", fullFieldName),
        controls
        ).toString();
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


  private String joinDot(String ... parts) {
    return Joiner.on(".").join(parts);
  }


  /**
   * Tworzy pole formularza razem z helpem, opisem itp.
   */
  private HtmlElement createControls(Class<?> fieldType, AnnotatedElement annotatedElement, String fieldName,
                                     Class<?> entityType, String fullFieldName, BootstrapRenderingBuilder builder,
                                     String... cssClasses) {
    return div(asList("col-sm-6", isCheckbox(fieldType) ? "checkbox" : ""),
        createInput(fieldType, annotatedElement, fullFieldName, entityType, fieldName, builder, cssClasses),
        el("span", asList("help-inline", bindServerErrorPath(fullFieldName, " ? '' : 'hide'")),
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


  /**
   * Tworzy kontrolkę formularza.
   */
  private HtmlPart createInput(Class<?> fieldType, AnnotatedElement annotatedElement, String fullFieldName,
                               Class<?> entityType, String fieldName, BootstrapRenderingBuilder builder,
                               String... cssClasses) {
    boolean addFormControlStyle = true;
    HtmlElement el;
    String ngModel = fullFieldName;

    if (Enum.class.isAssignableFrom(fieldType)) {
      el = el("select", asList(""),
            getEnumValuesElements((Class<? extends Enum<?>>) fieldType,
                                  !isAnnotationPresent(annotatedElement, NotNull.class)))
          .attr("ui-select2", null);
    } else if (Time.class.isAssignableFrom(fieldType)) {
      el = el("input", asList("input-size-time", "timepicker"))
          .attr("type", "text")
          .attr("placeholder", "HH:mm");
      pattern(el, "[0-2][0-9]:[0-5][0-9]");
    } else if (Date.class.isAssignableFrom(fieldType)) {
      el = el("input", asList("input-size-date"))
          .attr("type", "text")
          .attr("mt-datepicker", "datepickerOptions");
    } else if (isAnnotationPresent(annotatedElement, ManyToOne.class, EntityReference.class, OneToOne.class)) {
      String[] splitted = fullFieldName.split("[.]");
      String lastNameElement = lastNameElement(splitted);

      el = el("input", asList(""))
          .attr("ui-select2", joinDot(lastNameElement, "options"))
          .attr("ng-disabled", joinDot(lastNameElement, "options.disabled"));

      if (fieldType.equals(Integer.class)) {
        ngModel = joinDot(lastNameElement, "value");
      }
    } else if (isCheckbox(fieldType)) {
      el = el("label", asList(""),
                el("input", asList("")).attr("type", "checkbox"),
                text("&nbsp;"));

      addFormControlStyle = false;
    } else {
      el = el("input", asList(""))
          .attr("type", "text");

      if (Number.class.isAssignableFrom(fieldType)) {
        pattern(el, "[0-9]+([.,][0-9]+)?");
      }
    }

    el.attr("id", fullFieldName)
      .attr("name", fullFieldName.replaceAll("\\.", "_"))
      .attr("ng-model", ngModel)
      .attr("placeholder", "{{ '" + fullFieldName + "' | translate }}")
      .style(cssClasses);

    if (addFormControlStyle) {
      el.style("form-control");
    }

    addFieldValidation(fieldType, entityType, el, fieldName);

    if (builder != null) {
      for (String attr : builder.attributes.keySet()) {
        el.attr(attr, builder.attributes.get(attr));
      }
    }

    return el;
  }


  private boolean isAnnotationPresent(
      AnnotatedElement annotatedElement, Class<? extends Annotation> ... annotations) {
    if (annotatedElement != null) {
      for (Class<? extends Annotation> annotation : annotations) {
        if (annotatedElement.isAnnotationPresent(annotation)) {
          return true;
        }
      }
    }

    return false;
  }


  private void addFieldValidation(Class<?> fieldType, Class<?> entityType, HtmlElement el, String fieldName) {
    if (entityType == null) {
      return;
    }

    String propertyName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
    if (fieldName.contains(".")) {
      entityType = getPropertyType(entityType, fieldName.substring(0, fieldName.lastIndexOf(".")));
    }

    BeanDescriptor constraintsForClass = validator.getConstraintsForClass(entityType);
    javax.validation.metadata.PropertyDescriptor constraintsForProperty =
        constraintsForClass.getConstraintsForProperty(propertyName);

    if (constraintsForProperty != null) {
      Set<ConstraintDescriptor<?>> constraintDescriptors = constraintsForProperty.getConstraintDescriptors();

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


  private void pattern(HtmlElement el, String pattern) {
    el.attr("ng-pattern", "/^(" + pattern + ")?$/");
  }


  private HtmlPart[] getEnumValuesElements(Class<? extends Enum<?>> propertyType, boolean withEmptyElement) {
    List<HtmlPart> elements = new ArrayList<>();

    if (withEmptyElement) {
      elements.add(el("option", asList("")));
    }

    for (Enum<?> enumElement : propertyType.getEnumConstants()) {
      elements.add(el("option", asList(""))
                     .attr("translate", joinDot("enum", propertyType.getSimpleName(), enumElement.toString()))
                     .attr("value", enumElement.name()));
    }

    return elements.toArray(new HtmlPart[0]);
  }


  public BootstrapRenderingBuilder create(Class<?> entityType) {
    return new BootstrapRenderingBuilder(entityType);
  }


  /**
   * Klasa buildera pozwalająca na zdefiniowanie parametrów renderowania pól.
   */
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


    public BootstrapRenderingBuilder cssClasses(String ... cssClasses) {
      this.cssClasses = cssClasses;
      return this;
    }


    public BootstrapRenderingBuilder attribute(String name, String value) {
      attributes.put(name, value);
      return this;
    }


    public String render(String ... fields) {
      if (inline) {
        return renderer().renderInlineFields(fields, this);
      } else {
        return asList(fields).stream().reduce("", (sum, fieldName) -> {
          return sum + renderer().renderField(getPropertyType(entityType, fieldName),
              getAnnoatedElement(entityType, fieldName), fieldName, this) + "\n";
        });
      }
    }


    public String renderWithName(String entityFieldName, String htmlFieldName) {
      return renderer().renderField(getPropertyType(entityType, entityFieldName),
          getAnnoatedElement(entityType, entityFieldName), htmlFieldName, this);
    }
  }


  /**
   * Renderuje listę pól należących do encji <code>entityType</code>. Prefiksem nazw będzie
   * "entity" - jako generyczne odwowłanie do  w/w obiektu.
   */
  private String renderInlineFields(String[] fields, BootstrapRenderingBuilder builder) {
    List<HtmlPart> elements = new ArrayList<>();

    for (String fieldName : fields) {
      String fullFieldName = fullFieldName(builder.entityName, fieldName);
      String simpleElementName = last(fieldName.split("[.]"));

      elements.add(el("span", asList("inline-form-text", simpleElementName)).attr("translate", fullFieldName));

      elements.add(createInput(getPropertyType(builder.entityType, fieldName),
                                getAnnoatedElement(builder.entityType, fieldName),
                                fullFieldName, builder.entityType, "entity", builder,
                                // css classes
                                simpleElementName));
    }
    elements.remove(0);

    elements.add(el("span", asList("help-inline"), text(bindServerErrorPath(builder.entityName, ""))));

    return createControlGroup(fullFieldName(builder.entityName, fields[0]),
                                 div(asList("controls"), elements.toArray(new HtmlPart[0])));
  }


  /**
   * Renderuje pojedyncze pole typu <code>fieldType</code>, o nazwie <code>fieldName</code>, któremu
   * opcjonalnie można przypisać dodatkowe klasy stylu <code>cssClasses</code>.
   */
  public String renderSingleField(Class<?> fieldType, String fieldName, String ... cssClasses) {
    return renderField(fieldType, null, fieldName, create(null).cssClasses(cssClasses)) + "\n";
  }


  public String renderSingleField(Method method, String fieldName, String ... cssClasses) {
    return renderField(method.getReturnType(), method, fieldName, create(null).cssClasses(cssClasses)) + "\n";
  }


  private String fullFieldName(String entityName, String fieldName) {
    return (entityName != null ? decapitalize(entityName) + "." : "") + fieldName;
  }


  public HtmlElement div(List<String> classes, HtmlPart ... innerElements) {
    return new HtmlElement("div", innerElements).style(classes);
  }


  public HtmlElement el(String element, List<String> classes, HtmlPart ... innerElements) {
    return new HtmlElement(element, innerElements).style(classes);
  }


  public HtmlText text(String text) {
    return new HtmlText(text);
  }


  public static BootstrapRenderer renderer() {
    return bootstrapRenderer;
  }
}

