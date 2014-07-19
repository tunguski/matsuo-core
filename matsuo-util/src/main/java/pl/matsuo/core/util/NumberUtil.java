package pl.matsuo.core.util;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.math.BigDecimal.*;


/**
 * Pomocnicze metody przy operacjach na liczbach
 * @author Marek Romanowski
 * @since Jul 22, 2013
 */
public class NumberUtil {


  public static final BinaryOperator<BigDecimal> sumBigDecimal = (sum, add) -> sum.add(add);


  /**
   * Tworzy nowego {@link BigDecimal}a.
   */
  public static final <E> Function<String, E> createObject(Function<String, E> fn) {
    return (String value) -> {
      if (value == null) {
        return null;
      }

      return fn.apply(value);
    };
  }


  /**
   * Tworzy nowego {@link BigDecimal}a.
   */
  public static final BigDecimal bd(String value) {
    return createObject(BigDecimal::new).apply(value);
  }


  /**
   * Tworzy nowego {@link BigDecimal}a.
   */
  public static final BigDecimal bd(Integer value) {
    return BigDecimal.valueOf(value);
  }


  /**
   * Tworzy nowego {@link Integer}a.
   */
  public static final Integer i(String value) {
    return createObject(Integer::valueOf).apply(value);
  }


  /**
   * Tworzy nowego {@link Integer}a.
   */
  public static final Integer i(int value) {
    return value;
  }


  public static BigDecimal safeAddBD(BigDecimal bd1, BigDecimal bd2) {
    if (bd1 == null && bd2 == null) {
      return ZERO;
    } else if (bd2 == null) {
      return bd1;
    } else if (bd1 == null) {
      return bd2;
    } else {
      return bd1.add(bd2);
    }
  }
}

