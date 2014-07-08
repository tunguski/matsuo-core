package pl.matsuo.core.kv;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.parameterprovider.KeyValueParameterProvider;

import static javax.persistence.EnumType.*;
import static org.junit.Assert.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


public class TestKeyValueFacadeBuilder {
  private static final Logger logger = LoggerFactory.getLogger(TestKeyValueFacadeBuilder.class);


  private final FacadeBuilder facadeBuilder = new FacadeBuilder();
  private final TestKeyValueSubEntity keyValueEntity = new TestKeyValueSubEntity();
  private final ITestKeyValueFacade facade = facadeBuilder.createFacade(
      new KeyValueParameterProvider(keyValueEntity), ITestKeyValueFacade.class);


  @After
  public void logPrintState() {
    logger.info(keyValueEntity.getFields().toString());
  }


  @Test
  public void testIntegerReadWrite() throws Exception {
    facade.setInteger(7);
    assertEquals((Object) 7, facade.getInteger());
  }


  @Test
  public void testEnumReadWrite() throws Exception {
    facade.setEnumType(STRING);
    assertEquals(STRING, facade.getEnumType());
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
  public void tesDoubleToInteger() throws Exception {
    facade.setDoubleToInteger(7.3);
    assertEquals((Object) 7, facade.getDoubleToInteger());
  }


  @Test
  public void testDoubleToBigDecimal() throws Exception {
    facade.setDoubleToBigDecimal(7.7);
    assertEquals(bd("7.7"), facade.getDoubleToBigDecimal());
  }


  @Test
  public void testElements() throws Exception {
    keyValueEntity.getInternalElements().add(new TestKeyValueSubEntity());
    ITestKeyValueFacade keyValueElementFacade = facade.getInternalElements().get(0);
    keyValueElementFacade.setString("test2");
    assertEquals("test2", keyValueElementFacade.getString());
  }
}

