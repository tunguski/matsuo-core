package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import java.sql.Date;
import java.sql.Time;

public class CustomJacksonModule extends SimpleModule {
  private static final long serialVersionUID = 1L;

  public CustomJacksonModule() {
    super("Klinika", new Version(1, 0, 0, null, "pl.matsuo", "klinika"));
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);

    SimpleSerializers serializers = new SimpleSerializers();
    SimpleDeserializers deserializers = new SimpleDeserializers();

    // java.sql.Time
    serializers.addSerializer(Time.class, new TimeSerializer());
    deserializers.addDeserializer(Time.class, new TimeDeserializer());

    // java.sql.Date
    serializers.addSerializer(Date.class, new SqlDateSerializer());
    deserializers.addDeserializer(Date.class, new SqlDateDeserializer());

    context.addSerializers(serializers);
    context.addDeserializers(deserializers);
  }
}
