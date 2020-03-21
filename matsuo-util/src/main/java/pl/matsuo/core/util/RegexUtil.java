package pl.matsuo.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by marek on 09.05.14. */
public class RegexUtil {

  public static String findGroup(String text, Pattern pattern, int group) {
    Matcher matcher = pattern.matcher(text);
    matcher.find();
    return matcher.group(group);
  }
}
