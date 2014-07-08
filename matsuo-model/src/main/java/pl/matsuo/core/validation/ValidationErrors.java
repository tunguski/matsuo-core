package pl.matsuo.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ValidationErrors {


  private Map<String, String> fieldErrors = new HashMap<>();
  private List<String> globalErrors = new ArrayList<>();


  // getters
  public Map<String, String> getFieldErrors() {
    return fieldErrors;
  }
  public void setFieldErrors(Map<String, String> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }
  public List<String> getGlobalErrors() {
    return globalErrors;
  }
  public void setGlobalErrors(List<String> globalErrors) {
    this.globalErrors = globalErrors;
  }
}
