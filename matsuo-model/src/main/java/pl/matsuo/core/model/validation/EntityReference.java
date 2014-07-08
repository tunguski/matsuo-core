package pl.matsuo.core.model.validation;

import pl.matsuo.core.model.AbstractEntity;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


@Constraint(validatedBy={EntityReferenceValidator.class})
@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface EntityReference {
  Class<? extends AbstractEntity> value();


  String message() default "entity with that id does not exists";
  Class[] groups() default {};
  Class[] payload() default {};
}
