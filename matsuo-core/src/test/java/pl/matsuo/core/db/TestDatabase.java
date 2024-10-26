package pl.matsuo.core.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.ReflectUtil.getValue;

import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.Interceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.AuditTrailInterceptor;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;
import pl.matsuo.core.test.data.TestSessionState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DbConfig.class, TestSessionState.class})
public class TestDatabase {

  @Autowired protected Database database;
  @Autowired protected EntityInterceptorService entityInterceptorService;

  @Test
  public void testEntityInterceptor() {
    List<Interceptor> interceptors = getValue(entityInterceptorService, "interceptors");
    assertEquals(2, interceptors.size());

    boolean containsAuditTrailInterceptor = false;
    boolean containsIdBucketInterceptor = false;

    for (Interceptor interceptor : interceptors) {
      containsAuditTrailInterceptor =
          containsAuditTrailInterceptor
              || interceptor.getClass().equals(AuditTrailInterceptor.class);
      containsIdBucketInterceptor =
          containsIdBucketInterceptor || interceptor.getClass().equals(IdBucketInterceptor.class);
    }

    assertTrue(containsAuditTrailInterceptor);
    assertTrue(containsIdBucketInterceptor);
  }

  @Test
  @Transactional
  public void testBasicDatabaseOperations() {
    // FIXME: test sample basic CRUD operations
    Group group = new Group();
    group.setName("test-group");
    database.create(group);
    assertNotNull(group.getId());

    List<Group> groups = database.find(query(Group.class));
    assertTrue(groups.contains(group));

    groups = database.findAll(Group.class);
    assertTrue(groups.contains(group));

    group = database.findOne(query(Group.class, eq(Group::getName, "test-group")));
    assertNotNull(group);

    group = database.findById(Group.class, group.getId());
    assertNotNull(group);

    group.setName("test-group-2");
    database.update(group);
    assertNull(database.findOne(query(Group.class, eq(Group::getName, "test-group"))));
    assertNotNull(database.findOne(query(Group.class, eq(Group::getName, "test-group-2"))));

    database.delete(group);
    assertNull(database.findOne(query(Group.class, eq(Group::getName, "test-group"))));
    assertNull(database.findOne(query(Group.class, eq(Group::getName, "test-group-2"))));
  }
}
