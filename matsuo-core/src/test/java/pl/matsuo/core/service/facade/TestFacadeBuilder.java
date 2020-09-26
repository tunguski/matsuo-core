package pl.matsuo.core.service.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.matsuo.core.util.DateUtil.date;
import static pl.matsuo.core.util.NumberUtil.bd;
import static pl.matsuo.core.util.ReflectUtil.getValue;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import pl.matsuo.core.service.parameterprovider.AbstractParameterProvider;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;
import pl.matsuo.core.util.collection.CollectionUtil;

@Slf4j
public class TestFacadeBuilder {

  private final FacadeBuilder facadeBuilder = new FacadeBuilder();
  private final Map print = new HashMap();
  private final PrintTestFacade facade = facadeBuilder.createFacade(print, PrintTestFacade.class);

  @After
  public void logPrintState() {
    log.info(print.toString());
  }

  @Test
  public void testIntegerReadWrite() {
    facade.setInteger(7);
    assertEquals((Object) 7, facade.getInteger());
  }

  @Test
  public void testStringReadWrite() {
    facade.setString("test");
    assertEquals("test", facade.getString());
  }

  @Test
  public void testDateReadWrite() {
    facade.setDate(date(2013, 4, 4));
    assertEquals(date(2013, 4, 4), facade.getDate());
  }

  @Test
  public void testBigDecimalReadWrite() {
    facade.setBigDecimal(bd("700.43"));
    assertEquals(bd("700.43"), facade.getBigDecimal());
  }

  @Test
  public void testBooleanReadWrite() {
    facade.setBoolean(true);
    assertTrue(facade.getBoolean());
    facade.setBoolean(false);
    assertFalse(facade.getBoolean());
  }

  @Test
  public void testSubEntity() {
    PrintTestSubFacade subEntity = facade.getSubEntity();

    facade.setBigDecimal(bd("0.00"));
    subEntity.setBigDecimal(bd("333"));
    subEntity.setSubBigDecimal(bd("700.43"));
    assertEquals(bd("333"), subEntity.getBigDecimal());
    assertEquals(bd("700.43"), subEntity.getSubBigDecimal());
    assertEquals(bd("0.00"), facade.getBigDecimal());
    assertEquals(bd("333"), print.get("subEntity.bigDecimal"));
    assertEquals(bd("700.43"), print.get("subEntity.subBigDecimal"));
    assertEquals(bd("0.00"), print.get("bigDecimal"));
  }

  @Test
  public void testCreateParameterProvider() {
    IParameterProvider<String> parameterProvider =
        facadeBuilder.createParameterProvider(CollectionUtil.stringMap("1", "one", "2", "two"));
    assertEquals("one", parameterProvider.get("1"));
    assertEquals("two", parameterProvider.get("2"));
  }

  @Test
  public void testInitializeProviders() {
    facadeBuilder.initializeProviders();
    Map<Class, Class<? extends AbstractParameterProvider>> parameterProviders =
        getValue(facadeBuilder, "parameterProviders");
    assertEquals(2, parameterProviders.size());
  }
}
