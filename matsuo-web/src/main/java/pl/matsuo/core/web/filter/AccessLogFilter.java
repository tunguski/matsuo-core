package pl.matsuo.core.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    AccessLog accessLog = new AccessLog();
    accessLog.setIp(httpRequest.getRemoteAddr());
    accessLog.setRequest(httpRequest.getRequestURI());
    accessLog.setMethod(httpRequest.getMethod());
    if (!httpRequest.getParameterMap().keySet().isEmpty()) {
      accessLog.setParameters(httpRequest.getParameterMap().toString());
    }
    if (sessionState.getUser() != null) {
      accessLog.setIdUser(sessionState.getUser().getId());
    }

    database.create(accessLog);

    try {
      chain.doFilter(request, response);
    } finally {
      accessLog.setStatus(httpResponse.getStatus());
      try {
        if (httpResponse.getStatus() >= 300 && httpResponse.getStatus() < 400) {
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
