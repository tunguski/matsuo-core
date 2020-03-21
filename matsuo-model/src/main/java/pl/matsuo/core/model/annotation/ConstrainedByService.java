package pl.matsuo.core.model.annotation;

import pl.matsuo.core.service.ListProvider;

public @interface ConstrainedByService {

  Class<? extends ListProvider> value();
}
