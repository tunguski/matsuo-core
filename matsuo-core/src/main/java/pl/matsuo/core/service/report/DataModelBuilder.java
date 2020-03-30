package pl.matsuo.core.service.report;

import static java.util.Optional.ofNullable;
import static pl.matsuo.core.util.function.FunctionalUtil.*;

import java.util.HashMap;
import java.util.Map;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.service.db.Database;

public class DataModelBuilder {

  private final Database database;
  private final Map<String, Object> dataModel;

  public DataModelBuilder(Map<String, Object> dataModel, Database database) {
    this.database = database;
    this.dataModel = dataModel;
  }

  public DataModelBuilder() {
    this.database = null;
    this.dataModel = new HashMap<>();
  }

  public DataModelBuilder maybePut(
      Integer id, String propertyName, Class<? extends AbstractEntity> entityClass) {
    ofNullable(id)
        .ifPresent(
            idEntity -> dataModel.put(propertyName, database.findById(entityClass, idEntity)));
    return this;
  }

  public DataModelBuilder put(String propertyName, Object value) {
    dataModel.put(propertyName, value);
    return this;
  }

  public Map<String, Object> getDataModel() {
    return dataModel;
  }
}
