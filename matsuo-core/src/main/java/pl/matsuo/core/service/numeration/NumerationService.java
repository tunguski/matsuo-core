package pl.matsuo.core.service.numeration;

import java.util.Date;

/** Created by tunguski on 15.09.13. */
public interface NumerationService {

  String getNumber(String numeration, Date date, boolean preview);
}
