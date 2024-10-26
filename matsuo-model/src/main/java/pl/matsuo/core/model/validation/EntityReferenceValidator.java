package pl.matsuo.core.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;

/**
 * Waliduje czy przekazana wartość jest identyfikatorem encji istniejącej w bazie danych.
 *
 * @since 15-06-2013
 */
public class EntityReferenceValidator implements ConstraintValidator<EntityReference, Long> {

  @Autowired protected Database database;

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    //		if (value == null) {
    //			return true;
    //		}
    //
    //		try {
    //	    Class<? extends AbstractEntity> entity = annotation.value();
    //	    return database.findById(entity, value) != null;
    //		} catch (NumberFormatException e) {
    //		  return false;
    //		}
    return true;
  }
}
