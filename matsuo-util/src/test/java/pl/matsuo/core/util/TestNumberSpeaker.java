package pl.matsuo.core.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;


public class TestNumberSpeaker {


  @Test
  public void testCurrencySpeaker() throws Exception {
    checkSpeakCashAmount("1.00", "jeden złoty zero groszy");
    checkSpeakCashAmount("0.01", "jeden grosz");
    checkSpeakCashAmount("0.21", "dwadzieścia jeden groszy");
    checkSpeakCashAmount("1235234.02", "jeden milion dwieście trzydzieści pięć tysięcy dwieście trzydzieści cztery złote dwa grosze");
    checkSpeakCashAmount("17.57", "siedemnaście złotych pięćdziesiąt siedem groszy");
  }


  @Test
  public void testCurrencySpeakerWithNegativeValue() throws Exception {
    checkSpeakCashAmount("-7.7", "minus siedem złotych siedemdziesiąt groszy");
    checkSpeakCashAmount("-0.03", "minus trzy grosze");
  }


  protected void checkSpeakCashAmount(String value, String expected) throws Exception {
    assertEquals(expected, speakCashAmount(bd(value)));
  }

  @Test
  public void testSpeakNumber() throws Exception {
    checkSpeakNumber(1, "jeden");

  }


  protected void checkSpeakNumber(long value, String expected) throws Exception {
    assertEquals(expected, speakNumber(value));
  }
}

