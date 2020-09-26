package pl.matsuo.core.test.data;

import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.execution.IExecuteService;
import pl.matsuo.core.service.session.SessionState;

public abstract class AbstractTestData implements IExecuteService {

  @Autowired protected Database database;
  @Autowired protected SessionState sessionState;

  protected void withIdBucket(Long idBucket, Runnable runnable) {
    sessionState.setIdBucket(idBucket);
    try {
      runnable.run();
    } finally {
      sessionState.setIdBucket(null);
    }
  }

  protected void withUser(String userName, Runnable runnable) {
    User user = database.findOne(query(User.class, eq(User::getUsername, userName)));
    sessionState.setUser(user);
    sessionState.setIdBucket(user.getIdBucket());
    try {
      runnable.run();
    } finally {
      sessionState.setUser(null);
      sessionState.setIdBucket(null);
    }
  }

  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}
