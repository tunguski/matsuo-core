package pl.matsuo.core.service.numeration;

import java.util.Date;

public interface NumerationService {

  String getNumber(String numeration, Date date, boolean preview);
}
