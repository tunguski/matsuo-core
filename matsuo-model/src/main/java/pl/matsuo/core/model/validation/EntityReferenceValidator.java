package pl.matsuo.core.model.validation;

import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.validator.ValidatorBase;

/**
 * Waliduje czy przekazana wartość jest identyfikatorem encji istniejącej w bazie danych.
 *
 * @since 15-06-2013
 */
public class EntityReferenceValidator extends ValidatorBase<EntityReference, Integer> {

  @Autowired protected Database database;

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
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
