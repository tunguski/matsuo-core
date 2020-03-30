package pl.matsuo.core.web.controller;

import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.test.data.UserTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DbConfig.class, TestDataExecutionConfig.class, UserTestData.class})
public abstract class AbstractDbControllerRequestTest extends AbstractControllerRequestTest {

  @Autowired protected Database database;

  @Before
  public void setup() {
    super.setup();
    sessionState.setUser(
        database.findOne(
            query(User.class, eq(User::getUsername, "admin")).initializer(new UserInitializer())));
  }
}
