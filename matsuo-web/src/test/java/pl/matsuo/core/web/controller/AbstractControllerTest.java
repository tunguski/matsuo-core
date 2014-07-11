package pl.matsuo.core.web.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.facade.FacadeBuilderMethods;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;
import pl.matsuo.core.test.data.UserTestData;

import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 19.12.13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DbConfig.class, TestDataExecutionConfig.class, FacadeBuilder.class,
                                  EntityInterceptorService.class, IdBucketInterceptor.class,
                                  TestSessionState.class, UserTestData.class })
public abstract class AbstractControllerTest implements FacadeBuilderMethods {


  @Autowired
  protected Database database;
  @Autowired
  protected FacadeBuilder facadeBuilder;
  @Autowired
  protected SessionState sessionState;


  @Before
  public void setupSessionState() {
    sessionState.setUser(database.findOne(query(User.class, eq("username", "admin")).initializer(new UserInitializer())));
  }


  @Override
  public IFacadeBuilder getFacadeBuilder() {
    return facadeBuilder;
  }


  @Override
  public IParameterProvider<?> createParameterProvider(Object object) {
    return facadeBuilder.createParameterProvider(object);
  }
}

