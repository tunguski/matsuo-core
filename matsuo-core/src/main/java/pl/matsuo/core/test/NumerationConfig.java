package pl.matsuo.core.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.matsuo.core.service.numeration.MonthlyNumerationSchemaStrategy;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.service.numeration.QuaterlyNumerationSchemaStrategy;

@Configuration
@Import({
  NumerationServiceImpl.class,
  MonthlyNumerationSchemaStrategy.class,
  QuaterlyNumerationSchemaStrategy.class
})
public class NumerationConfig {}
