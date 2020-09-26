package pl.matsuo.core.web.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.permission.PermissionService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {
      DbConfig.class,
      PermissionService.class,
      PermissionsFilter.class,
      TestSessionState.class
    })
public class TestPermissionsFilter {

  @Autowired PermissionsFilter permissionsFilter;
  @Autowired SessionState sessionState;

  @Test
  public void testPassLoggedOff() throws IOException, ServletException {
    MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/index.html");
    MockHttpServletResponse servletResponse = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    permissionsFilter.doFilter(servletRequest, servletResponse, filterChain);

    assertEquals(OK.value(), servletResponse.getStatus());
    verify(filterChain).doFilter(servletRequest, servletResponse);
  }

  @Test
  public void testRejectLoggedOff() throws IOException, ServletException {
    MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/api/lists/AA");
    MockHttpServletResponse servletResponse = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    permissionsFilter.doFilter(servletRequest, servletResponse, filterChain);

    assertEquals(UNAUTHORIZED.value(), servletResponse.getStatus());
    verifyNoMoreInteractions(filterChain);
  }

  protected void configureSessionState(String groupName) {
    User user = new User();
    Group group = new Group();
    group.setName(groupName);
    user.getGroups().add(group);

    sessionState.setUser(user);
  }

  @Test
  @DirtiesContext
  public void testPassLoggedOn() throws IOException, ServletException {
    configureSessionState("ADMIN");

    MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/api/lists/AA");
    MockHttpServletResponse servletResponse = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    permissionsFilter.doFilter(servletRequest, servletResponse, filterChain);

    assertEquals(OK.value(), servletResponse.getStatus());
    verify(filterChain).doFilter(servletRequest, servletResponse);
  }

  @Test
  public void testRejectLoggedOn() throws IOException, ServletException {
    configureSessionState("REGISTRATION");

    MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/api_xxx/lists/AA");
    MockHttpServletResponse servletResponse = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    permissionsFilter.doFilter(servletRequest, servletResponse, filterChain);

    assertEquals(FORBIDDEN.value(), servletResponse.getStatus());
    verifyNoMoreInteractions(filterChain);
  }
}
