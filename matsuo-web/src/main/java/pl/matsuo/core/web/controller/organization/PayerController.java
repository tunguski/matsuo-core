package pl.matsuo.core.web.controller.organization;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.web.controller.AbstractSimpleController;

import java.util.List;

import static java.util.Arrays.*;


@RestController
@RequestMapping("/payers")
public class PayerController extends AbstractSimpleController<AbstractParty> {


  @Override
  protected List<String> queryMatchers() {
    return asList("firstName", "lastName", "fullName", "shortName", "code");
  }
}

