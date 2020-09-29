package pl.matsuo.core.util;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

/*
 * NumberSpeaker.java
 *
 * Created on 08.11.2004
 *
 * Copyright (c) 2004, Klaudiusz Kulik <kulikk@post.pl>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *   Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation
 *   and/or other materials  provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

/**
 * Zamiana liczb na postać słowną
 *
 * @author Klaudiusz Kulik kulikk(at)monstrum.org
 */
public abstract class NumberSpeaker {

  private static final String[] teens = {
    "",
    "jeden",
    "dwa",
    "trzy",
    "cztery",
    "pięć",
    "sześć",
    "siedem",
    "osiem",
    "dziewięć",
    "dziesięć",
    "jedenaście",
    "dwanaście",
    "trzynaście",
    "czternaście",
    "piętnaście",
    "szesnaście",
    "siedemnaście",
    "osiemnaście",
    "dziewiętnaście"
  };
  private static final String _20 = "dwadzieścia";
  private static final String _30 = "trzydzieści";
  private static final String _40 = "czterdzieści";
  private static final String _1e1 = "dziesiąt";
  private static final String _100 = "sto";
  private static final String _200 = "dwieście";
  private static final String _300 = "trzysta";
  private static final String _400 = "czterysta";
  private static final String _1e2 = "set";
  private static final String _1000 = "tysiąc";

  private static final String[] _1e3suff = {"ące", "ęcy"};
  private static final String[] _1e6suff = {"y", "ów"};

  private static final String _1e3 = "tysi";

  private static final String _1e6 = "milion";
  private static final String _1e9 = "miliard";
  private static final String _1e12 = "bilion";
  private static final String _1e15 = "biliard";
  private static final String _1e18 = "trylion";

  private static final String _1e21 = "tryliard";
  private static final String _1e24 = "kwadrylion";

  private static final String[] zloteSuffixes = {
    "złoty", // 1
    "złote", // 2-4
    "złotych" // >5
  };
  private static final String[] groszeSuffixes = {
    "grosz", // 1
    "grosze", // 2-4
    "groszy", // >5
    "groszy", // x1
  };

  private static final int secSize = 3; // po 3 cyfry w sekcji

  /**
   * Metoda zwracająca słowną reprezentację liczby
   *
   * @param value liczba
   * @return słowna reprezentacja
   */
  public static String speakNumber(long value) {
    StringBuffer buffer = new StringBuffer();
    String stringValue = String.valueOf(value);

    int length = stringValue.length();
    int steps = length / secSize;
    int remain = length % secSize;
    if (length < secSize) {
      steps = 0;
    }
    if (remain != 0) {
      steps++;
    }
    // Bierzemy poszczególne sekcje po 3 cyfry
    for (int i = 1; i <= steps; i++) {
      int begin = length - (i * secSize);
      if (begin < 0) {
        begin = 0;
      }
      int end = length - ((i - 1) * secSize);
      String subValue = stringValue.substring(begin, end);
      // tworzymy sekcje wraz z sufiksami
      buffer.insert(0, decode(subValue) + " " + determineSuffix(subValue, i) + " ");
    }

    return buffer.toString().trim();
  }

  public static String speakNumber(int value) {
    return speakNumber((long) value);
  }

  public static String speakNumber(byte value) {
    return speakNumber((long) value);
  }

  public static String speakNumber(String value) {
    return speakNumber(Long.parseLong(value));
  }

  /**
   * Metoda zamienia liczbę z przedziału 0-999 na jej słowną reprezentację
   *
   * @param number liczba do konwersji
   * @return napis ze słowną reprezentacją Metoda jest rekurencyjna! Wywołuje przeciążoną metodę.
   *     TODO: Uprościć
   */
  private static String decode(String number) {
    String retval;
    int num = Integer.parseInt(number);

    if (num < 20) {
      retval = (teens[num]);
    } else if ((num >= 20) && (num < 30)) {
      retval = (_20 + " " + teens[num - 20]);
    } else if ((num >= 30) && (num < 40)) {
      retval = (_30 + " " + teens[num - 30]);
    } else if ((num >= 40) && (num < 50)) {
      retval = (_40 + " " + teens[num - 40]);
    } else if ((num >= 50) && (num < 100)) {
      int i = num / 10;
      int j = num % 10;
      retval = (teens[i] + _1e1 + " " + teens[j]);
    } else if ((num >= 100) && (num < 200)) {
      retval = (_100 + " " + decode(num % 100));
    } else if ((num >= 200) && (num < 300)) {
      retval = (_200 + " " + decode(num % 200));
    } else if ((num >= 300) && (num < 400)) {
      retval = (_300 + " " + decode(num % 300));
    } else if ((num >= 400) && (num < 500)) {
      retval = (_400 + " " + decode(num % 400));
    } else if ((num >= 500) && (num < 1000)) {
      int i = num / 100;
      int j = num % 100;
      retval = (teens[i] + _1e2 + " " + decode(j));
    } else retval = null;
    return retval;
  }

  /** Przeciążona metoda konwertująca. Dodana w celu uproszczenia rekurencji */
  private static String decode(int number) {
    return decode(String.valueOf(number));
  }

  /**
   * Metoda określa przyrostki dodawane do poszczególnych trójek liczb.
   *
   * @param number liczba jako string
   * @param position pozycja danej trójki
   * @return napis zawierający prawidłowy przyrostek
   */
  private static String determineSuffix(String number, int position) {
    String suffix = "";
    if (number.equals("000")) {
      return suffix;
    }
    // pozycja 2 - tysiące, 3 - miliony, 4 - miliardy itd.
    switch (position) {
      case 1:
        return suffix;
      case 2: // tysiące
        if (number.endsWith("1") && (number.length() == 1)) suffix = _1000;
        if (!(number.endsWith("1") && number.length() == 1)) {
          suffix = _1e3;
          suffix += suffixHelper(number, _1e3suff);
        }
        return suffix;
      case 3: // miliony
        suffix = _1e6;
        break;
      case 4: // miliardy
        suffix = _1e9;
        break;
      case 5: // biliony
        suffix = _1e12;
        break;
      case 6: // biliardy
        suffix = _1e15;
        break;
      case 7: // tryliony
        suffix = _1e18;
        break;
    }
    // od miliona w górę odmiana regularna
    if (!(number.endsWith("1") && number.length() == 1)) suffix += suffixHelper(number, _1e6suff);
    return suffix;
  }

  /**
   * Metoda upraszczająca kod dla tworzenia sufisków
   *
   * @param number liczba jako string
   * @return napis zawierający końcówkę
   */
  private static String suffixHelper(String number, String[] suffixes) {

    if (number.equals("")) {
      return "";
    }
    if ((number.endsWith("11"))
        || (number.endsWith("12"))
        || (number.endsWith("13"))
        || (number.endsWith("14"))) {
      return suffixes[1];
    } else {
      if ((number.endsWith("2")) || (number.endsWith("3")) || (number.endsWith("4"))) {
        return suffixes[0];
      } else {
        return suffixes[1];
      }
    }
  }

  private static String currencyDeclension(long number, String[] suffixes) {
    long unities = number % 10;
    long tens = (number - unities) / 10 % 10;
    if (unities == 1)
      if (suffixes.length == 4 && tens > 0) return suffixes[3];
      else return suffixes[0];
    if (unities > 1 && unities < 5) return suffixes[1];
    if (unities == 0 || unities > 5) return suffixes[2];
    return null; // nigdy nie osiagane
  }

  public static String speakCashAmount(BigDecimal value) {
    String result = "";

    if (value.compareTo(ZERO) < 0) {
      result += "minus ";
      value = value.abs();
    }

    String upperPart = "";
    if (value.longValue() != 0)
      upperPart =
          speakNumber(value.longValue())
              + " "
              + currencyDeclension(value.longValue(), zloteSuffixes);
    long lowerPart = (long) (value.doubleValue() * 100 % 100);
    if (lowerPart == 0) result += upperPart + " zero ";
    else result += upperPart + " " + speakNumber(lowerPart) + " ";
    result += currencyDeclension(lowerPart, groszeSuffixes);
    return result.trim().replaceAll("  ", " ");
  }
}
