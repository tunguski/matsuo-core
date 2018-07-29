package pl.matsuo.core.service.db;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.util.function.FunctionalUtil.*;

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
    when(sessionState.getIdBucket()).thenReturn(10);
    doAnswer(invocation -> {
      AbstractQuery query = invocation.getArgument(0);
      with(AbstractQuery.class.getDeclaredField("sessionFactory"), field -> {
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
    }).when(beanFactory).autowireBeanProperties(anyObject(), anyInt(), anyBoolean());

    testUser.setIdBucket(10);
    testUser2.setIdBucket(11);
  }


  @Test
  public void testFindById() throws Exception {
    when(session.get(User.class, 7)).thenReturn(testUser);
    assertEquals(testUser, database.findById(User.class, 7));
  }


  @Test(expected = RuntimeException.class)
  public void testFindById2() throws Exception {
    when(session.get(User.class, 7)).thenReturn(testUser2);
    database.findById(User.class, 7);
  }


  @Test
  public void testFindAll() throws Exception {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> all = database.findAll(User.class);
    assertEquals(1, all.size());
    assertEquals(testUser, all.get(0));
  }


  @Test
  public void testInitializeEntity() throws Exception {
    AtomicBoolean invoked = new AtomicBoolean();
    database.initializeEntity(testUser, user -> invoked.set(true));
    assertTrue(invoked.get());
  }


  @Test
  public void testCreate() throws Exception {
    assertEquals(testUser2, database.create(testUser2));
    verify(session).save(testUser2);
  }


  @Test
  public void testUpdate() throws Exception {
    assertEquals(testUser2, database.update(testUser2));
    verify(session).update(testUser2);
  }


  @Test
  public void testDelete() throws Exception {
    database.delete(testUser2);
    verify(session).delete(testUser2);
  }


  @Test(expected = RuntimeException.class)
  public void testDelete1() throws Exception {
    when(session.get(User.class, 7)).thenReturn(testUser2);
    database.delete(User.class, 7);
    verify(session).delete(testUser2);
  }


  @Test
  public void testDelete2() throws Exception {
    when(session.get(User.class, 7)).thenReturn(testUser);
    database.delete(User.class, 7);
    verify(session).delete(testUser);
  }


  @Test
  public void testEvict() throws Exception {
    database.evict(testUser);
    verify(session).evict(testUser);
  }


  @Test
  public void testFind() throws Exception {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> users = database.find(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }


  @Test
  public void testFindAsAdmin() throws Exception {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    List<User> users = database.findAsAdmin(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }


  @Test
  public void testFindOne() throws Exception {
    org.hibernate.query.Query hQuery = mock(org.hibernate.query.Query.class);
    when(session.createQuery(anyString())).thenReturn(hQuery);
    when(hQuery.list()).thenReturn(asList(testUser));

    assertEquals(testUser, database.findOne(query(User.class, eq(User::getId, 7))));
  }
}

