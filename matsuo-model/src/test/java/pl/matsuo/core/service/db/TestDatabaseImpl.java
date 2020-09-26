package pl.matsuo.core.service.db;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.function.FunctionalUtil.with;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.session.SessionState;

public class TestDatabaseImpl {

  DatabaseImpl database = new DatabaseImpl();
  SessionFactory sessionFactory = mock(SessionFactory.class);
  AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
  SessionState sessionState = mock(SessionState.class);
  Session session = mock(Session.class);
  User testUser = new User();
  User testUser2 = new User();

  public TestDatabaseImpl() {
    database.sessionFactory = sessionFactory;
    database.sessionState = sessionState;
    database.beanFactory = beanFactory;

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(sessionState.getIdBucket()).thenReturn(10L);
    doAnswer(
            invocation -> {
              AbstractQuery query = invocation.getArgument(0);
              with(
                  AbstractQuery.class.getDeclaredField("sessionFactory"),
                  field -> {
                    field.setAccessible(true);
                    try {
                      field.set(query, sessionFactory);
                    } catch (IllegalAccessException e) {
                      throw new RuntimeException(e);
                    } finally {
                      field.setAccessible(false);
                    }
                  });

              return null;
            })
        .when(beanFactory)
        .autowireBeanProperties(anyObject(), anyInt(), anyBoolean());

    testUser.setIdBucket(10L);
    testUser2.setIdBucket(11L);
  }

  @Test
  public void testFindById() {
    when(session.get(User.class, 7L)).thenReturn(testUser);
    assertEquals(testUser, database.findById(User.class, 7L));
  }

  @Test(expected = RuntimeException.class)
  public void testFindById2() {
    when(session.get(User.class, 7L)).thenReturn(testUser2);
    database.findById(User.class, 7L);
  }

  @Test
  public void testFindAll() {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> all = database.findAll(User.class);
    assertEquals(1, all.size());
    assertEquals(testUser, all.get(0));
  }

  @Test
  public void testInitializeEntity() {
    AtomicBoolean invoked = new AtomicBoolean();
    database.initializeEntity(testUser, user -> invoked.set(true));
    assertTrue(invoked.get());
  }

  @Test
  public void testCreate() {
    assertEquals(testUser2, database.create(testUser2));
    verify(session).save(testUser2);
  }

  @Test
  public void testUpdate() {
    assertEquals(testUser2, database.update(testUser2));
    verify(session).update(testUser2);
  }

  @Test
  public void testDelete() {
    database.delete(testUser2);
    verify(session).delete(testUser2);
  }

  @Test(expected = RuntimeException.class)
  public void testDelete1() {
    when(session.get(User.class, 7L)).thenReturn(testUser2);
    database.delete(User.class, 7L);
    verify(session).delete(testUser2);
  }

  @Test
  public void testDelete2() {
    when(session.get(User.class, 7L)).thenReturn(testUser);
    database.delete(User.class, 7L);
    verify(session).delete(testUser);
  }

  @Test
  public void testEvict() {
    database.evict(testUser);
    verify(session).evict(testUser);
  }

  @Test
  public void testFind() {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> users = database.find(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }

  @Test
  public void testFindAsAdmin() {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> users = database.findAsAdmin(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }

  @Test
  public void testFindOne() {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    assertEquals(testUser, database.findOne(query(User.class, eq(User::getId, 7))));
  }
}
