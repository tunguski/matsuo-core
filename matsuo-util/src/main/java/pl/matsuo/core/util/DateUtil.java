package pl.matsuo.core.util;

import static java.util.Calendar.*;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.sql.Time;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings("deprecation")
public class DateUtil {

  public static final DateFormat isoDateFormat = new ISO8601DateFormat();

  public static final Format[] formats =
      new Format[] {
        new DateFormat() {
          @Override
          public StringBuffer format(
              Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            toAppendTo.append("" + date.getTime());
            return toAppendTo;
          }

          @Override
          public Date parse(String source, ParsePosition pos) {
            Date date = new Date(Long.valueOf(source));
            pos.setIndex(source.length() - 1);
            return date;
          }
        },
        isoDateFormat,
        DateTimeFormatter.ISO_INSTANT.toFormat()
      };

  public static Date date(int year, int month, int date) {
    return cal(year, month, date).getTime();
  }

  public static Date date(String source) {
    if (source.startsWith("\"")) {
      source = source.substring(1, source.length() - 1);
    }

    for (Format format : formats) {
      try {
        return (Date) format.parseObject(source);
      } catch (ParseException | NumberFormatException e) {
      }
    }

    throw new IllegalArgumentException("Could not parse " + source);
  }

  public static Date maybeDate(String source) {
    for (Format format : formats) {
      try {
        return (Date) format.parseObject(source);
      } catch (ParseException | NumberFormatException e) {
      }
    }

    return null;
  }

  public static Date date(int year, int month, int date, int hour, int minutes) {
    return cal(year, month, date, hour, minutes).getTime();
  }

  public static Date date(Date date, Integer hour, Integer minutes) {
    return cal(date, hour, minutes).getTime();
  }

  public static Date addTime(Date date, int field, int value) {
    Calendar cal = cal(date);
    cal.add(field, value);
    return cal.getTime();
  }

  public static Calendar cal(Date date) {
    if (date == null) {
      return null;
    } else {
      return cal(date.getTime());
    }
  }

  public static Calendar cal(Date date, Integer hour, Integer minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(HOUR_OF_DAY, hour);
    calendar.set(MINUTE, minutes);
    calendar.set(SECOND, 0);
    calendar.set(MILLISECOND, 0);
    return calendar;
  }

  public static Calendar cal(long millis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar;
  }

  public static Calendar cal(int year, int month, int date) {
    return new GregorianCalendar(year, month, date);
  }

  public static Calendar cal(int year, int month, int date, int hour, int minutes) {
    return new GregorianCalendar(year, month, date, hour, minutes);
  }

  public static Time time(int hour, int minutes) {
    return new Time(hour, minutes, 0);
  }

  public static Date dateAndTime(Date date, Time time) {
    return date(date, time.getHours(), time.getMinutes());
  }

  public static Calendar min(Calendar cal1, Calendar cal2) {
    return cal1.before(cal2) ? cal1 : cal2;
  }

  public static Calendar max(Calendar cal1, Calendar cal2) {
    return cal1.after(cal2) ? cal1 : cal2;
  }

  public static Date min(Date date1, Date date2) {
    return date1.before(date2) ? date1 : date2;
  }

  public static Date max(Date date1, Date date2) {
    return date1.after(date2) ? date1 : date2;
  }

  public static boolean between(Date date, Date from, Date to) {
    return (from == null || date.after(from)) && (to == null || date.before(to));
  }

  public static java.sql.Date sqlDate(Date date) {
    return new java.sql.Date(date.getTime());
  }

  public static java.sql.Date sqlDate(int year, int month, int date) {
    return sqlDate(cal(year, month, date).getTime());
  }

  public static java.sql.Date sqlDate(int year, int month, int date, int hour, int minutes) {
    return sqlDate(cal(year, month, date, hour, minutes).getTime());
  }

  public static java.sql.Date sqlDate(Date date, Integer hour, Integer minutes) {
    return sqlDate(cal(date, hour, minutes).getTime());
  }

  public static String isoFormat(Date date) {
    return isoDateFormat.format(date);
  }

  public static LocalDate localDate(Date date) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault())
        .toLocalDate();
  }

  public static Date getQuaterStart(Date date) {
    Calendar cal = cal(date, 0, 0);
    int quater = cal.get(MONTH) / 3;
    cal.set(MONTH, quater * 3);
    cal.set(DATE, 1);
    return cal.getTime();
  }

  public static Date getQuaterEnd(Date date) {
    Calendar cal = cal(date, 0, 0);
    int quater = cal.get(MONTH) / 3 + 1;
    cal.set(MONTH, quater * 3 - 1);
    cal.set(DATE, cal.getActualMaximum(DATE));
    return cal.getTime();
  }
}
