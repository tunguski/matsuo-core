package pl.matsuo.core.web.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.service.db.Database;

public class TestAbstractController {

  Database database = mock(Database.class);

  AbstractController controller =
      new AbstractController<User, IQueryRequestParams>() {
        @Override
        protected List<Function<User, String>> queryMatchers() {
          return asList(User::getUsername, User::getPassword);
        }

        @Override
        protected List<? extends Initializer<? super User>> entityInitializers() {
          return asList(user -> user.setUsername("tester"));
        }
      };

  @Before
  public void setup() {
    controller.database = database;
  }

  @Test
  public void testListQuery() {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");

    AbstractQuery query = controller.listQuery(params, q -> "test string");
    assertEquals(
        "FROM pl.matsuo.core.model.user.User user WHERE test string "
            + "AND (lower(username) like '%some%' OR lower(password) like '%some%') "
            + "AND (lower(username) like '%text%' OR lower(password) like '%text%')",
        query.printQuery());
  }

  @Test
  public void testList() {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");

    when(database.find(any(Query.class)))
        .then(
            invocation -> {
              AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
              assertEquals(
                  "FROM pl.matsuo.core.model.user.User user WHERE "
                      + "(lower(username) like '%some%' OR lower(password) like '%some%') "
                      + "AND (lower(username) like '%text%' OR lower(password) like '%text%')",
                  query.printQuery());

              return Collections.nCopies(100, new User());
            });

    assertEquals(100, controller.list(params).size());
  }

  @Test
  public void testList_limitAndOffset() {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");
    when(params.getLimit()).thenReturn(20);
    when(params.getOffset()).thenReturn(10);

    when(database.find(any(Query.class)))
        .then(
            invocation -> {
              SessionFactory sessionFactory = mock(SessionFactory.class);
              org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
              Session session = mock(Session.class);

              when(sessionFactory.getCurrentSession()).thenReturn(session);
              when(session.createQuery(anyString())).thenReturn(hQuery);

              AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];

              Field field = query.getClass().getDeclaredField("sessionFactory");
              try {
                field.setAccessible(true);
                field.set(query, sessionFactory);
              } finally {
                field.setAccessible(false);
              }

              query.query(null);

              verify(hQuery).setMaxResults(20);
              verify(hQuery).setFirstResult(10);

              return Collections.nCopies(5, new User());
            });

    assertEquals(5, controller.list(params).size());
  }

  @Test
  public void testList1() {
    when(database.find(any(Query.class)))
        .then(
            invocation -> {
              AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
              assertEquals(
                  "FROM pl.matsuo.core.model.user.User user WHERE test_x AND test_y",
                  query.printQuery());

              return Collections.nCopies(33, new User());
            });

    assertEquals(33, controller.list((Condition) q -> "test_x", q -> "test_y").size());
  }

  @Test
  public void testEntityQuery() {
    assertEquals(
        "FROM pl.matsuo.core.model.user.User user WHERE test_x AND test_y",
        controller.entityQuery(User.class, q -> "test_x", q -> "test_y").printQuery());
  }

  @Test
  public void testListByIds() {
    when(database.find(any(Query.class)))
        .then(
            invocation -> {
              AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
              assertEquals(
                  "FROM pl.matsuo.core.model.user.User user WHERE id in (1, 2, 3)",
                  query.printQuery());

              return Collections.nCopies(3, new User());
            });

    assertEquals(3, controller.listByIds(asList(1, 2, 3)).size());
  }

  @Test
  public void testFind() {
    User user = new User();
    when(database.findById(any(Class.class), any(Long.class), any(Initializer.class)))
        .thenReturn(user);
    assertTrue(controller.find(7L).getBody() == user);
  }

  @Test
  public void testEntityInitializers() {
    when(database.findById(any(Class.class), any(Long.class), any(Initializer.class)))
        .then(
            invocation -> {
              assertEquals(3, invocation.getArguments().length);

              Initializer initializer = (Initializer) invocation.getArguments()[2];

              User user = new User();
              initializer.init(user);
              assertEquals("tester", user.getUsername());

              return user;
            });
    assertEquals("tester", ((User) controller.find(7L).getBody()).getUsername());
  }

  @Test
  public void testCreate() {}

  @Test
  public void testUpdate() {}

  @Test
  public void testDelete() {}

  @Test
  public void testUpdate1() {}

  @Test
  public void testChildLocation() {}

  @Test
  public void testHttpEntity() {}
}
