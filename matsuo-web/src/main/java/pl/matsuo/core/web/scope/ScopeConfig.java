package pl.matsuo.core.web.scope;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Created by marek on 19.07.14. */
@Configuration
public class ScopeConfig {

  @Bean
  public static CustomScopeConfigurer registerScopes() {
    CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
    Map<String, Object> scopes = new HashMap<>();
    scopes.put("wideSession", new WideSessionScope());
    customScopeConfigurer.setScopes(scopes);
    return customScopeConfigurer;
  }
}
