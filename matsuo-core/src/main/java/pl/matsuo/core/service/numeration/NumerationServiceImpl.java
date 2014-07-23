package pl.matsuo.core.service.numeration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.report.DataModelBuilder;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Service
public class NumerationServiceImpl implements NumerationService {


  @Autowired
  protected Database database;
  //@Autowired
  protected Configuration freeMarkerConfiguration = new Configuration();
  @Autowired
  protected List<NumerationSchemaStrategy> numerationSchemaStrategies;


  @Override
  public String getNumber(String numerationCode, Date date, boolean preview) {
    Numeration numeration = getOrCreateNumeration(numerationCode, date);

    String number = generateNumber(numeration, date);

    if (!preview) {
      numeration.setValue(numeration.getValue() + 1);
      database.update(numeration);
    }

    return number;
  }


  protected String generateNumber(Numeration numeration, Date date) {
    StringWriter writer = new StringWriter();

    Calendar numerationCal = cal(numeration.getStartDate());
    Calendar eventCal = cal(date);

    Map<String, Object> dataModel = new DataModelBuilder()
        .put("value", "" + numeration.getValue())
        .put("numerationYear", "" + numerationCal.get(YEAR))
        .put("numerationMonth", "" + (numerationCal.get(MONTH) + 1))
        .put("numerationDate", "" + numerationCal.get(DATE))
        .put("eventYear", "" + eventCal.get(YEAR))
        .put("eventMonth", "" + (eventCal.get(MONTH) + 1))
        .put("eventDate", "" + eventCal.get(DATE))
        .getDataModel();

    runtimeEx(() -> new Template("" + numeration.getId() + "-" + numeration.getCode(),
        numeration.getPattern(), freeMarkerConfiguration).process(dataModel, writer));

    return writer.getBuffer().toString();
  }


  protected Numeration getOrCreateNumeration(String numerationCode, Date date) {
    Numeration numeration = database.findOne(query(Numeration.class, eq("code", numerationCode),
        or(isNull("startDate"), gt("startDate", date)), or(isNull("endDate"), gt("endDate", date))));

    return numeration != null ? numeration : createNumeration(numerationCode, date);
  }


  protected Numeration createNumeration(String numerationCode, Date date) {
    NumerationSchema numerationSchema = database.findOne(query(NumerationSchema.class, eq("code", numerationCode)));
    if (numerationSchema == null) {
      List<NumerationSchema> numerationSchemas =
          database.findAsAdmin(query(NumerationSchema.class, eq("code", numerationCode), isNull("idBucket")));
      if (numerationSchemas.isEmpty()) {
        throw new RuntimeException("Cannot find numeration schema for code: " + numerationCode);
      } else {
        numerationSchema = numerationSchemas.get(0);
      }
    }

    return createNumerationInstance(numerationSchema, date);
  }

  protected Numeration createNumerationInstance(NumerationSchema numerationSchema, Date date) {
    String creationStrategy = numerationSchema.getCreationStrategy();
    NumerationSchemaStrategy numerationSchemaStrategy = numerationSchemaStrategies.stream()
        .filter(strategy -> strategy.getClass().getSimpleName().equals(creationStrategy))
        .findFirst()
        .orElseThrow(() -> new RuntimeException());

    Numeration numeration = numerationSchemaStrategy.createNumeration(numerationSchema, date);

    database.create(numeration);

    return numeration;
  }
}
