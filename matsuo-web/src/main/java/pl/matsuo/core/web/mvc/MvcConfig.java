package pl.matsuo.core.web.mvc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import pl.matsuo.core.web.annotation.WebConfiguration;
import pl.matsuo.core.web.view.BootstrapRenderer;

@WebConfiguration
@Configuration
@Import({BootstrapRenderer.class, FacadeBuilderHandlerMethodArgumentResolver.class})
public class MvcConfig extends WebMvcConfigurationSupport
    implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    RequestMappingHandlerAdapter adapter =
        event.getApplicationContext().getBean(RequestMappingHandlerAdapter.class);

    try {
      FacadeBuilderHandlerMethodArgumentResolver facadeBuilderHandlerMethodArgumentResolver =
          event.getApplicationContext().getBean(FacadeBuilderHandlerMethodArgumentResolver.class);

      List<HandlerMethodArgumentResolver> argumentResolvers =
          new ArrayList<>(adapter.getArgumentResolvers());
      adapter.getCustomArgumentResolvers();
      argumentResolvers.remove(facadeBuilderHandlerMethodArgumentResolver);
      argumentResolvers.add(0, facadeBuilderHandlerMethodArgumentResolver);
      adapter.setArgumentResolvers(argumentResolvers);
    } catch (BeansException e) {
      // e.printStackTrace();
    }
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // default object to json conversion
    converters.add(converter());
    // utf-8 as default encoding for all the requests
    converters.add(stringHttpMessageConverter());
    addDefaultHttpMessageConverters(converters);
  }

  private StringHttpMessageConverter stringHttpMessageConverter() {
    StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
    stringConverter.setSupportedMediaTypes(asList(new MediaType("text", "plain", UTF_8)));
    stringConverter.setSupportedMediaTypes(asList(new MediaType("text", "html", UTF_8)));
    return stringConverter;
  }

  @Bean
  public ObjectMapper objectMapper(MappingJackson2HttpMessageConverter converter) {
    return converter.getObjectMapper();
  }

  @Bean
  public MappingJackson2HttpMessageConverter converter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = converter.getObjectMapper();

    objectMapper.setDateFormat(new CustomDateFormat());

    objectMapper.registerModule(new CustomJacksonModule());
    objectMapper.registerModule(new Hibernate6Module());
    objectMapper.setSerializationInclusion(NON_NULL);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    return converter;
  }
}
