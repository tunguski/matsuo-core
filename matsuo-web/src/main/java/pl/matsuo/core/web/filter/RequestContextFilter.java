package pl.matsuo.core.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
