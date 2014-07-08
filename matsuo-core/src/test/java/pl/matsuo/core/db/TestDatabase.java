package pl.matsuo.core.db;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.AuditTrailInterceptor;
import pl.matsuo.core.test.data.TestSessionState;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.ReflectUtil.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DbConfig.class, TestSessionState.class })
public class TestDatabase {


  @Autowired
  protected SessionFactory sessionFactory;
  @Autowired
  protected EntityInterceptorService entityInterceptorService;


  @Test
  public void testEntityInterceptor() throws Exception {
    List<Interceptor> interceptors = getValue(entityInterceptorService, "interceptors");
    assertEquals(1, interceptors.size());
    assertTrue(interceptors.get(0).getClass().equals(AuditTrailInterceptor.class));
  }


  @Test
  @Transactional
  public void testBasicDatabaseOperations() throws Exception {
    // FIXME: test sample basic CRUD operations
  }
}

