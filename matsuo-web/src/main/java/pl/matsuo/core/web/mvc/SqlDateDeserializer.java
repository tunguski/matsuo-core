package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class SqlDateDeserializer extends StdScalarDeserializer<Date> {
  private static final long serialVersionUID = 1L;


  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


  public SqlDateDeserializer() {
    super(Date.class);
  }


  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
      JsonProcessingException {
    try {
      return new Date(dateFormat.parse(jp.getText()).getTime());
    } catch (ParseException e) {
      throw new JsonParseException("Unhandled date " + jp.getText(), jp.getCurrentLocation(), e);
    }
  }
}

