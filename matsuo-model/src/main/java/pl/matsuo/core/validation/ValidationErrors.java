package pl.matsuo.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrors {

  private Map<String, String> fieldErrors = new HashMap<>();
  private List<String> globalErrors = new ArrayList<>();
}
