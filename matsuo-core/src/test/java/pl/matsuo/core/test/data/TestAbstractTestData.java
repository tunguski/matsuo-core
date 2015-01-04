package pl.matsuo.core.test.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.session.SessionState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TestAbstractTestData {


  @Mock
  Database database;
  @Mock
  SessionState sessionState;
  @Spy @InjectMocks
  TestData testData;


  @Test
  public void testDataInDatabase() {
    User user = new User();
    user.setIdBucket(17);
    when(database.findOne(any())).thenReturn(user);

    testData.execute();
    assertTrue(testData.invoked);

    verify(database).findOne(any());
    verify(sessionState).setUser(user);
    verify(sessionState).setIdBucket(17);

    verify(sessionState).setUser(null);
    verify(sessionState).setIdBucket(null);
  }


  public static class TestData extends AbstractTestData {

    public boolean invoked = false;

    @Override
    public void execute() {
      withUser("admin", () -> invoked = true);
    }
  }
}