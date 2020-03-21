package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeSerializer extends StdSerializer<Time> {

  private final DateFormat format = new SimpleDateFormat("HH:mm");

  public TimeSerializer() {
    super(Time.class);
  }

  @Override
  public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonGenerationException {
    jgen.writeString(format.format(value));
  }
}
