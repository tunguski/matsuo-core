package pl.matsuo.core.web.filter;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.matsuo.core.service.permission.IPermissionService;
import pl.matsuo.core.service.permission.PermissionService;

@Component("permissionsFilter")
public class PermissionsFilter extends AbstractFilter {

  @Autowired PermissionService permissionService;

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // FIXME: weryfikacja uprawnień - jeśli są, zostają przesłane dane
    if (!permissionService.isPermitted(
        request.getRequestURI(), IPermissionService.RequestType.valueOf(request.getMethod()))) {
      System.out.println("no permissions to: " + request.getRequestURI());

      // jeśli brak zalogowanego użytkownika, zwrócony zostaje status UNAUTHORIZED, jeśli użytkownik
      // jest zalogowany,
      // ale nie ma uprawnień do zasobu, zwrócony zostaje status FORBIDDEN
      response.setStatus(sessionState.getUser() == null ? UNAUTHORIZED.value() : FORBIDDEN.value());
    } else {
      chain.doFilter(request, response);
    }
  }
}
