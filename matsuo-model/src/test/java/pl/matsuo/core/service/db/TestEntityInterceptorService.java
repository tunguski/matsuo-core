package pl.matsuo.core.service.db;

import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

import org.hibernate.Interceptor;
import org.junit.Test;

public class TestEntityInterceptorService {

  EntityInterceptorService entityInterceptorService = new EntityInterceptorService();
  Interceptor interceptor = mock(Interceptor.class);

  public TestEntityInterceptorService() {
    entityInterceptorService.setInterceptors(asList(interceptor));
  }

  @Test
  public void testOnLoad() throws Exception {
    reset(interceptor);
    entityInterceptorService.onLoad(null, null, null, null, null);
    verify(interceptor).onLoad(null, null, null, null, null);
  }

  @Test
  public void testOnFlushDirty() throws Exception {
    reset(interceptor);
    entityInterceptorService.onFlushDirty(null, null, null, null, null, null);
    verify(interceptor).onFlushDirty(null, null, null, null, null, null);
  }

  @Test
  public void testOnSave() throws Exception {
    reset(interceptor);
    entityInterceptorService.onSave(null, null, null, null, null);
    verify(interceptor).onSave(null, null, null, null, null);
  }

  @Test
  public void testOnDelete() throws Exception {
    reset(interceptor);
    entityInterceptorService.onDelete(null, null, null, null, null);
    verify(interceptor).onDelete(null, null, null, null, null);
  }

  @Test
  public void testOnCollectionRecreate() throws Exception {
    reset(interceptor);
    entityInterceptorService.onCollectionRecreate(null, null);
    verify(interceptor).onCollectionRecreate(null, null);
  }

  @Test
  public void testOnCollectionRemove() throws Exception {
    reset(interceptor);
    entityInterceptorService.onCollectionRemove(null, null);
    verify(interceptor).onCollectionRemove(null, null);
  }

  @Test
  public void testOnCollectionUpdate() throws Exception {
    reset(interceptor);
    entityInterceptorService.onCollectionUpdate(null, null);
    verify(interceptor).onCollectionUpdate(null, null);
  }

  @Test
  public void testPreFlush() throws Exception {
    reset(interceptor);
    entityInterceptorService.preFlush(null);
    verify(interceptor).preFlush(null);
  }

  @Test
  public void testPostFlush() throws Exception {
    reset(interceptor);
    entityInterceptorService.postFlush(null);
    verify(interceptor).postFlush(null);
  }

  @Test
  public void testAfterTransactionBegin() throws Exception {
    reset(interceptor);
    entityInterceptorService.afterTransactionBegin(null);
    verify(interceptor).afterTransactionBegin(null);
  }

  @Test
  public void testBeforeTransactionCompletion() throws Exception {
    reset(interceptor);
    entityInterceptorService.beforeTransactionCompletion(null);
    verify(interceptor).beforeTransactionCompletion(null);
  }

  @Test
  public void testAfterTransactionCompletion() throws Exception {
    reset(interceptor);
    entityInterceptorService.afterTransactionCompletion(null);
    verify(interceptor).afterTransactionCompletion(null);
  }
}
