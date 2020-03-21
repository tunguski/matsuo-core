package pl.matsuo.core.web.controller.exception;

import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.matsuo.core.exception.RestProcessingException;

@ControllerAdvice
public class RestProcessingExceptionHandler {

  @ExceptionHandler(RestProcessingException.class)
  public ResponseEntity<Object> handleException(
      final RestProcessingException e, WebRequest request) {

    Map<String, Object> result = new HashMap<>();
    List<String> globalErrors = new ArrayList<>();
    result.put("globalErrors", globalErrors);
    Map<String, String> fieldErrors = new HashMap<>();
    result.put("fieldErrors", fieldErrors);

    for (String key : e.getFieldErrors().keySet()) {
      fieldErrors.put(key, e.getFieldErrors().get(key));
    }

    for (String objectError : e.getGlobalErrors()) {
      globalErrors.add(objectError);
    }

    return new ResponseEntity<Object>(result, BAD_REQUEST);
  }
}
