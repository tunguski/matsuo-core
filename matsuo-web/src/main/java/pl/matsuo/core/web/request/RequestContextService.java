package pl.matsuo.core.web.request;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

@Service
public class RequestContextService {

  ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

  public String getContextPath() {
    return requestThreadLocal.get().getContextPath();
  }

  public void setRequest(HttpServletRequest request) {
    requestThreadLocal.set(request);
  }

  public void clearRequest() {
    requestThreadLocal.remove();
  }

  public String href(String path) {
    return getContextPath() + path;
  }

  public RedirectView redirect(String path) {
    return new RedirectView(getContextPath() + path);
  }
}
