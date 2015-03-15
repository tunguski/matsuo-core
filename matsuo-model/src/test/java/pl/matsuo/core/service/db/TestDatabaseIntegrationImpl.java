package pl.matsuo.core.service.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;

import static pl.matsuo.core.model.query.QueryBuilder.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DbConfig.class, EntityInterceptorService.class, IdBucketInterceptor.class,
                                  TestSessionState.class })
public class TestDatabaseIntegrationImpl {


  @Autowired
  protected Database database;
  @Autowired
  protected SessionState sessionState;


  @Test
  public void testIntegration() {
    database.find(query(User.class));
  }
}

