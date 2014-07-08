package pl.matsuo.core.service.numeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.service.db.Database;

import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Service
public class NumerationServiceImpl implements NumerationService {


  @Autowired
  protected Database database;


  @Override
  public String getPreviewNumber(String numerationCode) {
    Numeration numeration = database.findOne(query(Numeration.class, eq("code", numerationCode)));
    return numeration.getPattern().replaceAll("\\$", "" + numeration.getValue());
  }


  @Override
  public String getNumber(String numerationCode) {
    Numeration numeration = database.findOne(query(Numeration.class, eq("code", numerationCode)));
    Integer value = numeration.getValue();

    numeration.setValue(numeration.getValue() + 1);
    database.update(numeration);

    return numeration.getPattern().replaceAll("\\$", "" + value);
  }
}
