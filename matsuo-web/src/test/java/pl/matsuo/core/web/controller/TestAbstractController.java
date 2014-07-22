package pl.matsuo.core.web.controller;

import org.junit.Before;
import org.junit.Test;
import pl.matsuo.core.IQueryRequestParams;
import pl.matsuo.core.model.Initializer;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.Query;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class TestAbstractController {


  Database database = mock(Database.class);

  AbstractController controller = new AbstractController<User, IQueryRequestParams>() {
    @Override
    protected List<String> queryMatchers() {
      return asList("name", "index");
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
  public void testListQuery() throws Exception {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");

    AbstractQuery query = controller.listQuery(params, q -> "test string");
    assertEquals("FROM pl.matsuo.core.model.user.User user WHERE test string " +
            "AND (lower(name) like '%some%' OR lower(index) like '%some%') " +
            "AND (lower(name) like '%text%' OR lower(index) like '%text%')",
        query.printQuery());
  }

  @Test
  public void testList() throws Exception {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");

    when(database.find(any(Query.class))).then(invocation -> {
      AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
      assertEquals("FROM pl.matsuo.core.model.user.User user WHERE " +
              "(lower(name) like '%some%' OR lower(index) like '%some%') " +
              "AND (lower(name) like '%text%' OR lower(index) like '%text%')",
          query.printQuery());

      return Collections.nCopies(100, new User());
    });

    assertEquals(100, controller.list(params).size());
  }


  @Test
  public void testList_limit() throws Exception {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");
    when(params.getLimit()).thenReturn(20);
    when(params.getOffset()).thenReturn(10);

    when(database.find(any(Query.class))).thenReturn(Collections.nCopies(100, new User()));

    assertEquals(20, controller.list(params).size());
  }


  @Test
  public void testList_limitHigherThanDbResult() throws Exception {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");
    when(params.getLimit()).thenReturn(20);
    when(params.getOffset()).thenReturn(10);

    when(database.find(any(Query.class))).thenReturn(Collections.nCopies(15, new User()));

    assertEquals(5, controller.list(params).size());
  }


  @Test
  public void testList_offsetHigherThanDbResult() throws Exception {
    IQueryRequestParams params = mock(IQueryRequestParams.class);
    when(params.getQuery()).thenReturn("some text");
    when(params.getLimit()).thenReturn(20);
    when(params.getOffset()).thenReturn(30);

    when(database.find(any(Query.class))).thenReturn(Collections.nCopies(15, new User()));

    assertEquals(0, controller.list(params).size());
  }


  @Test
  public void testList1() throws Exception {
    when(database.find(any(Query.class))).then(invocation -> {
      AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
      assertEquals("FROM pl.matsuo.core.model.user.User user WHERE test_x AND test_y",
          query.printQuery());

      return Collections.nCopies(33, new User());
    });

    assertEquals(33, controller.list(q -> "test_x", q -> "test_y").size());
  }

  @Test
  public void testEntityQuery() throws Exception {
    assertEquals("FROM pl.matsuo.core.model.user.User user WHERE test_x AND test_y",
        controller.entityQuery(q -> "test_x", q -> "test_y").printQuery());
  }

  @Test
  public void testListByIds() throws Exception {
    when(database.find(any(Query.class))).then(invocation -> {
      AbstractQuery query = (AbstractQuery) invocation.getArguments()[0];
      assertEquals("FROM pl.matsuo.core.model.user.User user WHERE id in (1, 2, 3)",
          query.printQuery());

      return Collections.nCopies(3, new User());
    });

    assertEquals(3, controller.listByIds(asList(1, 2, 3)).size());
  }

  @Test
  public void testFind() throws Exception {
    User user = new User();
    when(database.findById(any(Class.class), any(Integer.class), any(Initializer[].class))).thenReturn(user);
    assertTrue(controller.find(7).getBody() == user);
  }

  @Test
  public void testEntityInitializers() throws Exception {
    when(database.findById(any(Class.class), any(Integer.class), any(Initializer.class))).then(invocation -> {
      assertEquals(3, invocation.getArguments().length);

      Initializer initializer = (Initializer) invocation.getArguments()[2];

      User user = new User();
      initializer.init(user);
      assertEquals("tester", user.getUsername());

      return user;
    });
    assertEquals("tester", ((User) controller.find(7).getBody()).getUsername());
  }

  @Test
  public void testCreate() throws Exception {

  }

  @Test
  public void testUpdate() throws Exception {

  }

  @Test
  public void testDelete() throws Exception {

  }

  @Test
  public void testUpdate1() throws Exception {

  }

  @Test
  public void testChildLocation() throws Exception {

  }

  @Test
  public void testHttpEntity() throws Exception {

  }
}

