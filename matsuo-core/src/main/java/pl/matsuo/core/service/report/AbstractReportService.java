package pl.matsuo.core.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.i18n.I18nService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.*;


/**
 * Serwis tworzenia raportu z wizyt.
 * @author Marek Romanowski
 * @since Aug 24, 2013
 */
@Service
public abstract class AbstractReportService<E> implements IReportService<E> {


  @Autowired
  protected Database database;
  @Autowired
  protected I18nService i18nService;
  /**
   * Default template file name. It is lowercased class name with "Service" cut from the end.
   */
  private String defaultTemplateName = getDefaultTemplateName();


  /**
   * Na podstawie przekazanych parametrów wyszukiwania wizyt tworzy model dla generowania druku.
   */
  @Override
  public final Map<String, Object> buildModel(E params) {
    Map<String, Object> dataModel = new HashMap<>();
    injectModel(new DataModelBuilder(dataModel, database), params);

    dataModel.put("params", params);
    dataModel.put("messages", i18nService);
    dataModel.put("generationTime", new Date());

    return dataModel;
  }


  @Override
  public String getName(E params) {
    return getTemplateName();
  }


  @Override
  public String getTemplateName() {
    return defaultTemplateName;
  }


  protected abstract void injectModel(DataModelBuilder dataModel, E params);


  private String getDefaultTemplateName() {
    String className = getClass().getSimpleName();
    return (className.length() > 7 && className.endsWith("Service")) ?
        uncapitalize(className.substring(0, className.length() - 7)) : "__noTemplate__";
  }
}

