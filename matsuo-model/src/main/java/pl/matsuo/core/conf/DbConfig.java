package pl.matsuo.core.conf;

import static java.lang.System.currentTimeMillis;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.matsuo.core.service.db.DatabaseImpl;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.AuditTrailInterceptor;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;

@Slf4j
@Configuration
@Import({
  DatabaseImpl.class,
  EntityInterceptorService.class,
  AuditTrailInterceptor.class,
  IdBucketInterceptor.class
})
@EnableTransactionManagement
public class DbConfig {

  @Autowired(required = false)
  @Qualifier("prod")
  DataSource dataSource;

  @Autowired(required = false)
  @Qualifier("prod")
  FactoryBean<SessionFactory> sessionFactory;

  @Autowired(required = false)
  @Qualifier("prod")
  PlatformTransactionManager transactionManager;

  @Bean(name = "default.dataSource")
  @Qualifier("app-ds")
  public DataSource dataSource() {
    if (dataSource != null) {
      return dataSource;
    } else {
      return createDataSource(DbConfig::getDatabaseUrl, getClass());
    }
  }

  public static DataSource createDataSource(
      Function<Properties, String> urlProvider, Class<?> resourceProvider) {
    BasicDataSource dataSource = new BasicDataSource();

    Properties properties = getDbProperties(resourceProvider);

    log.warn("Test environment!");
    dataSource.setDriverClassName(properties.getProperty("db.default.driverClassName"));
    dataSource.setUrl(urlProvider.apply(properties));
    dataSource.setUsername(properties.getProperty("db.default.username"));
    dataSource.setPassword(properties.getProperty("db.default.password"));

    return dataSource;
  }

  private static String getDatabaseUrl(Properties properties) {
    return properties
        .getProperty("db.default.url")
        .replace("test_db", "test_db_" + currentTimeMillis());
  }

  @Bean
  @Qualifier("app-entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      @Qualifier("app-ds") DataSource dataSource,
      EntityInterceptorService entityInterceptorService) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("pl.matsuo");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    Properties dbProperties = getDbProperties(getClass());
    dbProperties.put("hibernate.ejb.interceptor", entityInterceptorService);
    em.setJpaProperties(dbProperties);

    return em;
  }

  private static Properties getDbProperties(Class<?> aClass) {
    try {
      Properties properties = new Properties();
      properties.load(aClass.getResourceAsStream("/db.default.properties"));
      return properties;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean(name = "default.transactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("app-entityManagerFactory")
          LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    if (transactionManager != null) {
      return transactionManager;
    } else {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
      return transactionManager;
    }
  }
}
