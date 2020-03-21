package pl.matsuo.core.test.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

@Order(0)
public abstract class AbstractMediqTestData extends AbstractTestData {

  @Autowired MediqTestData mediqTestData;

  @Override
  public final void execute() {
    mediqTestData.withIdBucket(() -> internalExecute());
  }

  public abstract void internalExecute();
}
