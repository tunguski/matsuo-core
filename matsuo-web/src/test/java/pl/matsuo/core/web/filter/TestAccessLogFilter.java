package pl.matsuo.core.web.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import javax.servlet.FilterChain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.matsuo.core.model.log.AccessLog;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.test.data.TestSessionState;

@RunWith(MockitoJUnitRunner.class)
public class TestAccessLogFilter {

  @Mock Database database;
  @Mock PermissionService permissionService;
  @Mock TestSessionState sessionState;
  @Mock FilterChain chain;
  @InjectMocks AccessLogFilter accessLogFilter = new AccessLogFilter();

  @Test
  public void testDoFilter() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    accessLogFilter.doFilter(request, response, chain);

    verify(database).create(any(AccessLog.class));
    verify(chain).doFilter(request, response);
  }
}
