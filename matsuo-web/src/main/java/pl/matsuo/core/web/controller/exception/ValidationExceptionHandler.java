package pl.matsuo.core.web.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.matsuo.core.validation.ValidationErrors;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class ValidationExceptionHandler {


  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException pe) {
    BindingResult bindingResult = pe.getBindingResult();
    ValidationErrors errors = new ValidationErrors();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      errors.getFieldErrors().put(fieldError.getField(), fieldError.getCode());
    }

    for (ObjectError error : bindingResult.getGlobalErrors()) {
      errors.getGlobalErrors().add(error.getCode());
    }

    return new ResponseEntity<Object>(errors, BAD_REQUEST);
  }
}

