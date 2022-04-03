package pl.matsuo.core.service.numeration;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.ge;
import static pl.matsuo.core.model.query.QueryBuilder.isNull;
import static pl.matsuo.core.model.query.QueryBuilder.le;
import static pl.matsuo.core.model.query.QueryBuilder.or;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.DateUtil.cal;
import static pl.matsuo.core.util.collection.CollectionUtil.findFirst;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.numeration.NumerationSchema;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.report.DataModelBuilder;

@Service
public class NumerationServiceImpl implements NumerationService {

  @Autowired protected Database database;
  @Autowired protected List<NumerationSchemaStrategy> numerationSchemaStrategies;

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

    Map<String, Object> dataModel =
        new DataModelBuilder()
            .put("value", "" + numeration.getValue())
            .put("numerationYear", "" + numerationCal.get(YEAR))
            .put("numerationMonth", "" + (numerationCal.get(MONTH) + 1))
            .put("numerationDate", "" + numerationCal.get(DATE))
            .put("eventYear", "" + eventCal.get(YEAR))
            .put("eventMonth", "" + (eventCal.get(MONTH) + 1))
            .put("eventDate", "" + eventCal.get(DATE))
            .getDataModel();

    String generated =
        numeration
            .getPattern()
            .replace("${value}", "" + numeration.getValue())
            .replace("${numerationYear}", "" + numerationCal.get(YEAR))
            .replace("${numerationMonth}", "" + (numerationCal.get(MONTH) + 1))
            .replace("${numerationDate}", "" + numerationCal.get(DATE))
            .replace("${eventYear}", "" + eventCal.get(YEAR))
            .replace("${eventMonth}", "" + (eventCal.get(MONTH) + 1))
            .replace("${eventDate}", "" + eventCal.get(DATE));

    writer.write(generated);

    return writer.getBuffer().toString();
  }

  protected Numeration getOrCreateNumeration(String numerationCode, Date date) {
    Numeration numeration =
        database.findOne(
            query(
                Numeration.class,
                eq(Numeration::getCode, numerationCode),
                or(isNull(Numeration::getStartDate), le(Numeration::getStartDate, date)),
                or(isNull(Numeration::getEndDate), ge(Numeration::getEndDate, date))));

    return numeration != null ? numeration : createNumeration(numerationCode, date);
  }

  protected Numeration createNumeration(String numerationCode, Date date) {
    NumerationSchema numerationSchema =
        database.findOne(
            query(NumerationSchema.class, eq(NumerationSchema::getCode, numerationCode)));
    if (numerationSchema == null) {
      List<NumerationSchema> numerationSchemas =
          database.findAsAdmin(
              query(
                  NumerationSchema.class,
                  eq(NumerationSchema::getCode, numerationCode),
                  isNull(NumerationSchema::getIdBucket)));
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
    NumerationSchemaStrategy numerationSchemaStrategy =
        findFirst(
                numerationSchemaStrategies,
                strategy -> strategy.getClass().getSimpleName().equals(creationStrategy))
            .orElseThrow(RuntimeException::new);

    Numeration numeration = numerationSchemaStrategy.createNumeration(numerationSchema, date);

    database.create(numeration);

    return numeration;
  }
}
