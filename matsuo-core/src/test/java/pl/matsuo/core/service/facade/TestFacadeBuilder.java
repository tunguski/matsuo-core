package pl.matsuo.core.service.facade;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;
import pl.matsuo.core.util.collection.CollectionUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


public class TestFacadeBuilder {
  private static final Logger logger = LoggerFactory.getLogger(TestFacadeBuilder.class);


  private final FacadeBuilder facadeBuilder = new FacadeBuilder();
  private final Map print = new HashMap();
  private final PrintTestFacade facade = facadeBuilder.createFacade(print, PrintTestFacade.class);


  @After
  public void logPrintState() {
    logger.info(print.toString());
  }


  @Test
  public void testIntegerReadWrite() throws Exception {
    facade.setInteger(7);
    assertEquals((Object) 7, facade.getInteger());
  }


  @Test
  public void testStringReadWrite() throws Exception {
    facade.setString("test");
    assertEquals("test", facade.getString());
  }


  @Test
  public void testDateReadWrite() throws Exception {
    facade.setDate(date(2013, 4, 4));
    assertEquals(date(2013, 4, 4), facade.getDate());
  }


  @Test
  public void testBigDecimalReadWrite() throws Exception {
    facade.setBigDecimal(bd("700.43"));
    assertEquals(bd("700.43"), facade.getBigDecimal());
  }

  @Test
  public void testBooleanReadWrite() throws Exception {
    facade.setBoolean(true);
    assertTrue(facade.getBoolean());
    facade.setBoolean(false);
    assertFalse(facade.getBoolean());
  }


  @Test
  public void testSubEntity() throws Exception {
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
  public void testCreateParameterProvider() throws Exception {
    IParameterProvider<String> parameterProvider =
        facadeBuilder.createParameterProvider(CollectionUtil.stringMap("1", "one", "2", "two"));
    assertEquals("one", parameterProvider.get("1"));
    assertEquals("two", parameterProvider.get("2"));
  }


  @Test
  public void testInitializeProviders() throws Exception {
    facadeBuilder.initializeProviders();
  }
}

