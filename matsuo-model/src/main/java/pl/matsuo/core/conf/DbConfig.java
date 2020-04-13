package pl.matsuo.core.conf;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.matsuo.core.service.db.DatabaseImpl;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.AuditTrailInterceptor;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;

@Configuration
@Import({
  DatabaseImpl.class,
  EntityInterceptorService.class,
  AuditTrailInterceptor.class,
  IdBucketInterceptor.class
})
@EnableTransactionManagement
public class DbConfig {
  private static Logger logger = LoggerFactory.getLogger(DbConfig.class);

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
  public DataSource dataSource() throws IOException {
    if (dataSource != null) {
      return dataSource;
    } else {
      return createDataSource(DbConfig::getDatabaseUrl, getClass());
    }
  }

  public static DataSource createDataSource(
      Function<Properties, String> urlProvider, Class<?> resourceProvider) throws IOException {
    BasicDataSource dataSource = new BasicDataSource();

    Properties properties = new Properties();
    properties.load(resourceProvider.getResourceAsStream("/db.default.properties"));

    logger.warn("Test environment!");
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

  @Bean(name = "default.sessionFactory")
  @Qualifier("app-sessionFactory")
  public FactoryBean<SessionFactory> sessionFactoryBean(
      EntityInterceptorService interceptor, @Qualifier("app-ds") DataSource dataSource)
      throws IOException {
    if (sessionFactory != null) {
      return sessionFactory;
    } else {
      LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
      sessionFactoryBean.setDataSource(dataSource);
      sessionFactoryBean.setPackagesToScan("pl.matsuo.**.model");

      Properties properties = new Properties();
      properties.load(getClass().getResourceAsStream("/db.default.properties"));
      sessionFactoryBean.setHibernateProperties(properties);
      sessionFactoryBean.setEntityInterceptor(interceptor);

      return sessionFactoryBean;
    }
  }

  @Bean(name = "default.transactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("app-sessionFactory") SessionFactory sessionFactory) {
    if (transactionManager != null) {
      return transactionManager;
    } else {
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      transactionManager.setSessionFactory(sessionFactory);
      return transactionManager;
    }
  }
}
