package pl.matsuo.core.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.matsuo.core.model.log.AccessLog;
import pl.matsuo.core.service.permission.PermissionService;

@Component("accessLogFilter")
public class AccessLogFilter extends AbstractFilter {

  @Autowired PermissionService permissionService;

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    AccessLog accessLog = new AccessLog();
    accessLog.setIp(request.getRemoteAddr());
    accessLog.setRequest(request.getRequestURI());
    accessLog.setMethod(request.getMethod());
    if (!request.getParameterMap().keySet().isEmpty()) {
      accessLog.setParameters(request.getParameterMap().toString());
    }
    if (sessionState.getUser() != null) {
      accessLog.setIdUser(sessionState.getUser().getId());
    }

    database.create(accessLog);

    try {
      chain.doFilter(request, response);
    } finally {
      accessLog.setStatus(response.getStatus());
      try {
        if (response.getStatus() >= 300 && response.getStatus() < 400) {
          database.delete(accessLog);
        } else {
          database.update(accessLog);
        }
      } catch (RuntimeException e) {
        // FIXME: obsługa błędu zapisu statusu
      }
    }
  }
}
