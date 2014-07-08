package pl.matsuo.core.web.controller.log;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.log.AccessLog;
import pl.matsuo.core.web.controller.AbstractSimpleController;

import java.util.List;

import static java.util.Arrays.*;


/**
 * Created by tunguski on 12.01.14.
 */
@RestController
@RequestMapping("/accessLogs")
public class AccessLogController extends AbstractSimpleController<AccessLog> {


  @Override
  protected List<String> queryMatchers() {
    return asList("request", "method", "ip");
  }
}

