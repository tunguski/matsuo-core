package pl.matsuo.core.util;

import static org.junit.Assert.assertEquals;
import static pl.matsuo.core.util.NumberSpeaker.speakCashAmount;
import static pl.matsuo.core.util.NumberSpeaker.speakNumber;
import static pl.matsuo.core.util.NumberUtil.bd;

import org.junit.Test;

public class TestNumberSpeaker {

  @Test
  public void testCurrencySpeaker() {
    checkSpeakCashAmount("1.00", "jeden złoty zero groszy");
    checkSpeakCashAmount("0.01", "jeden grosz");
    checkSpeakCashAmount("0.21", "dwadzieścia jeden groszy");
    checkSpeakCashAmount(
        "1235234.02",
        "jeden milion dwieście trzydzieści pięć tysięcy dwieście trzydzieści cztery złote dwa grosze");
    checkSpeakCashAmount("17.57", "siedemnaście złotych pięćdziesiąt siedem groszy");
  }

  @Test
  public void testCurrencySpeakerWithNegativeValue() {
    checkSpeakCashAmount("-7.7", "minus siedem złotych siedemdziesiąt groszy");
    checkSpeakCashAmount("-0.03", "minus trzy grosze");
  }

  protected void checkSpeakCashAmount(String value, String expected) {
    assertEquals(expected, speakCashAmount(bd(value)));
  }

  @Test
  public void testSpeakNumber() {
    checkSpeakNumber(1, "jeden");
  }

  protected void checkSpeakNumber(long value, String expected) {
    assertEquals(expected, speakNumber(value));
  }
}
