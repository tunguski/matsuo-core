package pl.matsuo.core.conf;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.matsuo.core.service.db.DatabaseImpl;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.AuditTrailInterceptor;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * Created by tunguski on 19.09.13.
 */
@Configuration
@EnableTransactionManagement
public class DbConfig {


  private static int i = 0;


  @Bean public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
    // i++ to hack aby rozdzielić bazy pomiędzy testami uruchamianymi równolegle; bez tego są deadlocki
    dataSource.setUrl("jdbc:hsqldb:mem:test_" + i++ + ";hsqldb.tx=MVCC");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }


  @Bean
  public LocalSessionFactoryBean sessionFactoryBean(EntityInterceptorService interceptor) {
    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource());
    sessionFactoryBean.setPackagesToScan("pl.matsuo.**.model");
    sessionFactoryBean.setHibernateProperties(new Properties());
    sessionFactoryBean.getHibernateProperties().put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    sessionFactoryBean.getHibernateProperties().put("hibernate.hbm2ddl.auto", "update");
    sessionFactoryBean.getHibernateProperties().put("hibernate.jdbc.batch_size", "50");
//    sessionFactoryBean.getHibernateProperties().put("hibernate.id.new_generator_mappings", "true");

    // FIXME: zbieranie wszystkich interceptorów i tworzenie grupującego interceptora
    sessionFactoryBean.setEntityInterceptor(interceptor);

    return sessionFactoryBean;
  }


  @Bean public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory);
    return transactionManager;
  }


  @Bean public static BeanFactoryPostProcessor database() {
    return new ClassesAddingBeanFactoryPostProcessor(DatabaseImpl.class, EntityInterceptorService.class,
        AuditTrailInterceptor.class, IdBucketInterceptor.class);
  }
}

