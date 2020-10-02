package pl.matsuo.core.service.db;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.function.FunctionalUtil.with;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.session.SessionState;

public class TestDatabaseImpl {

  DatabaseImpl database = new DatabaseImpl();
  EntityManager entityManager =
      mock(EntityManager.class, withSettings().defaultAnswer(RETURNS_MOCKS));
  AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
  SessionState sessionState = mock(SessionState.class);
  User testUser = new User();
  User testUser2 = new User();

  public TestDatabaseImpl() {
    database.entityManager = entityManager;
    database.sessionState = sessionState;
    database.beanFactory = beanFactory;

    when(sessionState.getIdBucket()).thenReturn(10L);
    doAnswer(
            invocation -> {
              AbstractQuery query = invocation.getArgument(0);
              with(
                  AbstractQuery.class.getDeclaredField("entityManager"),
                  field -> {
                    field.setAccessible(true);
                    try {
                      field.set(query, entityManager);
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
    when(entityManager.find(User.class, 7L)).thenReturn(testUser);
    assertEquals(testUser, database.findById(User.class, 7L));
  }

  @Test(expected = RuntimeException.class)
  public void testFindById2() {
    when(entityManager.find(User.class, 7L)).thenReturn(testUser2);
    database.findById(User.class, 7L);
  }

  @Test
  public void testFindAll() {
    TypedQuery<User> typedQuery = mock(TypedQuery.class);
    when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(asList(testUser));

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
    verify(entityManager).persist(testUser2);
  }

  @Test
  public void testUpdate() {
    database.update(testUser2);
    verify(entityManager).merge(testUser2);
  }

  @Test
  public void testDelete() {
    database.delete(testUser2);
    verify(entityManager).remove(testUser2);
  }

  @Test(expected = RuntimeException.class)
  public void testDelete1() {
    when(entityManager.find(User.class, 7L)).thenReturn(testUser2);
    database.delete(User.class, 7L);
    verify(entityManager).remove(testUser2);
  }

  @Test
  public void testDelete2() {
    when(entityManager.find(User.class, 7L)).thenReturn(testUser);
    database.delete(User.class, 7L);
    verify(entityManager).remove(testUser);
  }

  @Test
  public void testEvict() {
    database.evict(testUser);
    verify(entityManager).detach(testUser);
  }

  @Test
  public void testFind() {
    setupForStringQueryTest();

    List<User> users = database.find(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }

  @Test
  public void testFindAsAdmin() {
    setupForStringQueryTest();

    List<User> users = database.findAsAdmin(query(User.class, eq(User::getId, 7)));
    assertEquals(1, users.size());
    assertEquals(testUser, users.get(0));
  }

  @Test
  public void testFindOne() {
    setupForStringQueryTest();

    assertEquals(testUser, database.findOne(query(User.class, eq(User::getId, 7))));
  }

  private void setupForStringQueryTest() {
    Query query = mock(Query.class);
    when(entityManager.createQuery(anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(asList(testUser));
  }
}
