package pl.matsuo.core.util;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.toLowerCase;

/** Pomocnicze metody dla obsługi Stringów. */
public class StringUtil {

  /**
   * Sprawdza czy przekazany obiekt nie jest pusty. Tekst składający się z samych białych znaków
   * jest uznawany za pusty.
   */
  public static boolean notEmpty(String text) {
    return text != null && !text.trim().isEmpty();
  }

  public static String camelCaseToCssName(String text) {
    text = toLowerCase(text.charAt(0)) + text.substring(1);
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      if (isLowerCase(text.charAt(i))) {
        sb.append(text.charAt(i));
      } else {
        sb.append("-" + toLowerCase(text.charAt(i)));
      }
    }

    return sb.toString();
  }
}
