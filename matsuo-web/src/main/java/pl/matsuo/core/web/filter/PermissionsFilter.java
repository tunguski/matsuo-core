package pl.matsuo.core.web.filter;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.matsuo.core.service.permission.IPermissionService;
import pl.matsuo.core.service.permission.PermissionService;

@Component("permissionsFilter")
public class PermissionsFilter extends AbstractFilter {

  @Autowired PermissionService permissionService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // FIXME: weryfikacja uprawnień - jeśli są, zostają przesłane dane
    if (!permissionService.isPermitted(
        httpRequest.getRequestURI(),
        IPermissionService.RequestType.valueOf(httpRequest.getMethod()))) {
      System.out.println("no permissions to: " + httpRequest.getRequestURI());

      // jeśli brak zalogowanego użytkownika, zwrócony zostaje status UNAUTHORIZED, jeśli użytkownik
      // jest zalogowany,
      // ale nie ma uprawnień do zasobu, zwrócony zostaje status FORBIDDEN
      httpResponse.setStatus(
          sessionState.getUser() == null ? UNAUTHORIZED.value() : FORBIDDEN.value());
    } else {
      chain.doFilter(request, response);
    }
  }
}
