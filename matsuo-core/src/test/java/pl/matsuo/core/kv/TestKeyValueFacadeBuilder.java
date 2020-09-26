package pl.matsuo.core.kv;

import static javax.persistence.EnumType.STRING;
import static org.junit.Assert.assertEquals;
import static pl.matsuo.core.util.DateUtil.date;
import static pl.matsuo.core.util.NumberUtil.bd;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.parameterprovider.KeyValueParameterProvider;

@Slf4j
public class TestKeyValueFacadeBuilder {

  private final FacadeBuilder facadeBuilder = new FacadeBuilder();
  private final TestKeyValueSubEntity keyValueEntity = new TestKeyValueSubEntity();
  private final ITestKeyValueFacade facade =
      facadeBuilder.createFacade(
          new KeyValueParameterProvider(keyValueEntity), ITestKeyValueFacade.class);

  @After
  public void logPrintState() {
    log.info(keyValueEntity.getFields().toString());
  }

  @Test
  public void testIntegerReadWrite() {
    facade.setInteger(7);
    assertEquals((Object) 7, facade.getInteger());
  }

  @Test
  public void testEnumReadWrite() {
    facade.setEnumType(STRING);
    assertEquals(STRING, facade.getEnumType());
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
  public void tesDoubleToInteger() {
    facade.setDoubleToInteger(7.3);
    assertEquals((Object) 7, facade.getDoubleToInteger());
  }

  @Test
  public void testDoubleToBigDecimal() {
    facade.setDoubleToBigDecimal(7.7);
    assertEquals(bd("7.7"), facade.getDoubleToBigDecimal());
  }

  @Test
  public void testElements() {
    keyValueEntity.getInternalElements().add(new TestKeyValueSubEntity());
    ITestKeyValueFacade keyValueElementFacade = facade.getInternalElements().get(0);
    keyValueElementFacade.setString("test2");
    assertEquals("test2", keyValueElementFacade.getString());
  }
}
