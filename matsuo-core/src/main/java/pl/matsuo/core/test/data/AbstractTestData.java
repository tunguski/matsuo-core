package pl.matsuo.core.test.data;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.execution.IExecuteService;


public abstract class AbstractTestData implements IExecuteService {


  @Autowired
  protected Database database;


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}

