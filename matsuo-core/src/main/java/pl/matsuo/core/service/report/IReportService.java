package pl.matsuo.core.service.report;

import java.util.Map;


public interface IReportService<E> {

  Map<String, Object> buildModel(E params);

  String getName(E params);

  String getTemplateName();
}

