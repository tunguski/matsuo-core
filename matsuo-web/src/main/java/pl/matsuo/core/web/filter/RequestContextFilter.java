package pl.matsuo.core.web.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.matsuo.core.web.request.RequestContextService;

@Component("requestContextFilter")
public class RequestContextFilter extends AbstractFilter {

  @Autowired RequestContextService requestContextService;

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      requestContextService.setRequest(request);
      chain.doFilter(request, response);
    } finally {
      requestContextService.clearRequest();
    }
  }
}
