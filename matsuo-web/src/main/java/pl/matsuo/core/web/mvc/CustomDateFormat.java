package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormat extends ISO8601DateFormat {
  private static final long serialVersionUID = 1L;

  protected final DateFormat[] formats = new DateFormat[] {new SimpleDateFormat("yy-MM-dd")};

  @Override
  public Date parse(String source, ParsePosition pos) {
    // index must be set to other than 0, I would swear this requirement is not there in
    // some version of jdk 6.
    // pos.setIndex(source.length());

    try {
      return ISO8601Utils.parse(source, pos);
    } catch (Exception e) {
      for (DateFormat dateFormat : formats) {
        try {
          return dateFormat.parse(source);
        } catch (ParseException e1) {
          // do notin'
        }
      }

      throw new RuntimeException(e);
    }
  }
}
