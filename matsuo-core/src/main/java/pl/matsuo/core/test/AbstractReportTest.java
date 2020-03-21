package pl.matsuo.core.test;

import static pl.matsuo.core.model.query.QueryBuilder.*;

import java.util.Map;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.user.initializer.UserInitializer;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.execution.ExecutionServiceImpl;
import pl.matsuo.core.service.report.IReportService;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.test.data.TestSessionState;
import pl.matsuo.core.test.data.UserTestData;

@Transactional
@ContextConfiguration(
    classes = {
      DbConfig.class,
      TestSessionState.class,
      ExecutionServiceImpl.class,
      TestDataExecutionConfig.class,
      UserTestData.class
    })
public abstract class AbstractReportTest<E> extends AbstractPrintGeneratingTest<E> {

  @Autowired protected SessionState sessionState;
  @Autowired protected Database database;
  @Autowired protected IReportService<E> reportService;

  @Override
  protected Map<String, Object> buildModel(E params) {
    return reportService.buildModel(params);
  }

  @Before
  public void setupSessionState() {
    sessionState.setUser(
        database.findOne(
            query(User.class, eq(User::getUsername, "admin")).initializer(new UserInitializer())));
  }
}
