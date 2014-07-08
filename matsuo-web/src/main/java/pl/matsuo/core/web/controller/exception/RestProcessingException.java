package pl.matsuo.core.web.controller.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.*;


/**
 * Created by tunguski on 16.09.13.
 */
public class RestProcessingException extends RuntimeException {


  protected List<String> globalErrors = new ArrayList<>();
  protected Map<String, String> fieldErrors = new HashMap<>();


  public RestProcessingException(String ... globalErrors) {
    this.globalErrors.addAll(asList(globalErrors));
  }


  public RestProcessingException fieldError(String field, String code) {
    fieldErrors.put(field, code);
    return this;
  }


  public RestProcessingException globalError(String code) {
    globalErrors.add(code);
    return this;
  }


  public List<String> getGlobalErrors() {
    return globalErrors;
  }
  public Map<String, String> getFieldErrors() {
    return fieldErrors;
  }
}

