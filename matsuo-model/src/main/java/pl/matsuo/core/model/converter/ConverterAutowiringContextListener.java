package pl.matsuo.core.model.converter;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

@Component
public class ConverterAutowiringContextListener
    implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired private Set<Converter<?, ?>> converters;
  @Autowired private ConversionService conversionService;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    GenericConversionService gcs = (GenericConversionService) conversionService;
    for (Converter<?, ?> converter : converters) {
      gcs.addConverter(converter);
    }
  }
}
