package pl.matsuo.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

  public static String findGroup(String text, Pattern pattern, int group) {
    Matcher matcher = pattern.matcher(text);
    matcher.find();
    return matcher.group(group);
  }
}
