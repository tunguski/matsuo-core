package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class SqlDateSerializer extends StdSerializer<Date> {


  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



  public SqlDateSerializer() {
    super(Date.class);
  }


  @Override
  public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    jgen.writeString(dateFormat.format(value));
  }
}

