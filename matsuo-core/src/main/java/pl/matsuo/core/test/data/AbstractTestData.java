package pl.matsuo.core.test.data;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.execution.IExecuteService;
import pl.matsuo.core.service.session.SessionState;


public abstract class AbstractTestData implements IExecuteService {


  @Autowired
  protected Database database;


  public void withIdBucket(SessionState sessionState, Integer idBucket, Runnable runnable) {
    sessionState.setIdBucket(idBucket);
    try {
      runnable.run();
    } finally {
      sessionState.setIdBucket(null);
    }
  }


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}

