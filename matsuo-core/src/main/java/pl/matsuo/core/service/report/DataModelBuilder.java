package pl.matsuo.core.service.report;

import static pl.matsuo.core.util.function.FunctionalUtil.*;

import java.util.HashMap;
import java.util.Map;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.service.db.Database;

/** Created by marek on 01.04.14. */
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
    optional(id)
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
