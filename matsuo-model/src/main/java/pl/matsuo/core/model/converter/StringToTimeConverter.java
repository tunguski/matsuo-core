package pl.matsuo.core.model.converter;

import static pl.matsuo.core.util.NumberUtil.i;

import java.sql.Time;
import org.springframework.stereotype.Component;

@Component
public class StringToTimeConverter extends AutowiringConverter<String, Time> {

  @SuppressWarnings("deprecation")
  @Override
  public Time convert(String time) {
    if (time == null) {
      return null;
    }

    String[] splitted = time.split(":");
    return new Time(i(splitted[0]), i(splitted[1]), 0);
  }
}
