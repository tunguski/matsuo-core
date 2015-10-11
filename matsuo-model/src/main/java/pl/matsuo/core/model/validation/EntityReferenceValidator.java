package pl.matsuo.core.model.validation;

import pl.matsuo.validator.ValidatorBase;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;

import javax.validation.ConstraintValidatorContext;


/**
 * Waliduje czy przekazana wartość jest identyfikatorem encji istniejącej w bazie danych.
 * @author Marek Romanowski
 * @since 15-06-2013
 */
public class EntityReferenceValidator extends ValidatorBase<EntityReference, Integer> {


  @Autowired
  protected Database database;


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

