package pl.matsuo.core.web.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.db.EntityInterceptorService;
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
public abstract class AbstractControllerTest extends AbstractDbTest implements FacadeBuilderMethods {
}

