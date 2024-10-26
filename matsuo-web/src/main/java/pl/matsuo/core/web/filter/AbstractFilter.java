package pl.matsuo.core.web.filter;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.session.SessionState;

public abstract class AbstractFilter extends HttpFilter {

  @Autowired protected SessionState sessionState;
  @Autowired Database database;

  private FilterConfig filterConfig;

  @Override
  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  @Override
  public void destroy() {}
}
