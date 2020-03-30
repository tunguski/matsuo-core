package pl.matsuo.core.model.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.matsuo.core.model.util.EntityUtil.createOrUpdate;
import static pl.matsuo.core.model.util.EntityUtil.maybeCreate;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;

public class TestEntityUtil {

  @Test
  public void testMaybeCreate() throws Exception {
    Database database = mock(Database.class);

    maybeCreate(new User(), d -> d.create(new Group())).accept(database);

    verify(database, times(2)).create(any());
  }

  @Test
  public void testCreateOrUpdate() throws Exception {
    Database database = mock(Database.class);

    AtomicBoolean value = new AtomicBoolean();

    createOrUpdate(new User(), entity -> value.set(true)).accept(database);

    verify(database).create(any(User.class));
    assertTrue(value.get());
  }

  @Test
  public void testCreateOrUpdate1() throws Exception {
    Database database = mock(Database.class);

    AtomicBoolean value = new AtomicBoolean();

    User user = new User();
    user.setId(7);
    createOrUpdate(user, entity -> value.set(true)).accept(database);

    verify(database).update(any(User.class));
    assertTrue(value.get());
  }

  @Test
  public void testCreateOrUpdate2() throws Exception {
    Database database = mock(Database.class);

    AtomicBoolean onCreate = new AtomicBoolean();
    AtomicBoolean onUpdate = new AtomicBoolean();

    createOrUpdate(new User(), entity -> onCreate.set(true), entity -> onUpdate.set(true))
        .accept(database);

    verify(database).create(any(User.class));
    assertTrue(onCreate.get());
    assertFalse(onUpdate.get());
  }

  @Test
  public void testCreateOrUpdate3() throws Exception {
    Database database = mock(Database.class);

    AtomicBoolean onCreate = new AtomicBoolean();
    AtomicBoolean onUpdate = new AtomicBoolean();

    User user = new User();
    user.setId(7);
    createOrUpdate(user, entity -> onCreate.set(true), entity -> onUpdate.set(true))
        .accept(database);

    verify(database).update(any(User.class));
    assertFalse(onCreate.get());
    assertTrue(onUpdate.get());
  }
}
