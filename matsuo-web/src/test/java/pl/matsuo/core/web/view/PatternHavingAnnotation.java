package pl.matsuo.core.web.view;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.constraints.Pattern;

/** Created by marek on 10.06.14. */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Pattern(regexp = "[0-9]{11}")
@Constraint(validatedBy = {})
public @interface PatternHavingAnnotation {
  String message() default "test pattern having annotation";

  Class[] groups() default {};

  Class[] payload() default {};
}
