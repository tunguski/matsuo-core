package pl.matsuo.core.web.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Pattern;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Pattern(regexp = "[0-9]{11}")
@Constraint(validatedBy = {})
public @interface PatternHavingAnnotation {
  String message() default "test pattern having annotation";

  Class[] groups() default {};

  Class[] payload() default {};
}
