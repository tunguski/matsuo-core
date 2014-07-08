package pl.matsuo.core.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import pl.matsuo.core.conf.ClassesAddingBeanFactoryPostProcessor;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.web.annotation.WebConfiguration;
import pl.matsuo.core.web.view.BootstrapRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.*;


@WebConfiguration
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport implements ApplicationListener<ContextRefreshedEvent> {


  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    RequestMappingHandlerAdapter adapter = event.getApplicationContext().getBean(RequestMappingHandlerAdapter.class);
    FacadeBuilderHandlerMethodArgumentResolver facadeBuilderHandlerMethodArgumentResolver =
      event.getApplicationContext().getBean(FacadeBuilderHandlerMethodArgumentResolver.class);

    List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(adapter.getArgumentResolvers());
    List<HandlerMethodArgumentResolver> customResolvers = adapter.getCustomArgumentResolvers();
    argumentResolvers.remove(facadeBuilderHandlerMethodArgumentResolver);
    argumentResolvers.add(0, facadeBuilderHandlerMethodArgumentResolver);
    adapter.setArgumentResolvers(argumentResolvers);
  }


  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(converter());
    addDefaultHttpMessageConverters(converters);
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
    objectMapper.registerModule(new Hibernate4Module());
    objectMapper.setSerializationInclusion(NON_NULL);
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    return converter;
  }


  @Bean
  public BeanFactoryPostProcessor mvcServices() {
    return new ClassesAddingBeanFactoryPostProcessor(BootstrapRenderer.class, FacadeBuilder.class,
                                                      FacadeBuilderHandlerMethodArgumentResolver.class);
  }
}

