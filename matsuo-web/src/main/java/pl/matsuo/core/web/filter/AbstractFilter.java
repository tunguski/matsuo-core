package pl.matsuo.core.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.session.SessionState;

/** Created by tunguski on 22.12.13. */
public abstract class AbstractFilter implements Filter {

  @Autowired protected SessionState sessionState;
  @Autowired Database database;

  private FilterConfig filterConfig;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }

  @Override
  public void destroy() {}
}
