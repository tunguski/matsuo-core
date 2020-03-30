package pl.matsuo.core.web.controller.log;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.Function;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.log.AccessLog;
import pl.matsuo.core.web.controller.AbstractSimpleController;

@RestController
@RequestMapping("/accessLogs")
public class AccessLogController extends AbstractSimpleController<AccessLog> {

  @Override
  protected List<Function<AccessLog, String>> queryMatchers() {
    return asList(AccessLog::getRequest, AccessLog::getMethod, AccessLog::getIp);
  }
}
