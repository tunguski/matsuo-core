package pl.matsuo.core.model.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface PasswordField {
}
