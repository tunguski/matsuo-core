package pl.matsuo.core.service.execution;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import pl.matsuo.core.model.execution.Execution;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.session.SessionState;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.System.*;
import static org.springframework.core.Ordered.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Serwis wykonujący zdefiniowane operacje.
 * @author Marek Romanowski
 * @since 11-07-2013
 */
@Service
public class ExecutionServiceImpl implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
  private static final Logger logger = LoggerFactory.getLogger(ExecutionServiceImpl.class);


  @Autowired(required = false)
  List<IExecuteService> executeServices;
  @Autowired
  Database database;
  @Autowired
  PlatformTransactionManager transactionManager;
  @Autowired
  SessionState sessionState;
  ApplicationContext applicationContext;


  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!event.getApplicationContext().equals(applicationContext)
        || executeServices == null || executeServices.isEmpty()) {
      return;
    }

    Collections.sort(executeServices, new AnnotationAwareOrderComparator() {
      @Override
      protected int getOrder(Object obj) {
        int order = super.getOrder(obj);

        return order == LOWEST_PRECEDENCE ? HIGHEST_PRECEDENCE : order;
      }
    });

    for (IExecuteService executeService : executeServices) {
      String executeServiceName = executeService.getExecuteServiceName();

      TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

      Execution execution = new Execution();
      execution.setBeanName(executeServiceName);
      execution.setRunDate(new Date());
      execution.setSuccess(true);

      try {
        boolean noSuccessExecutions =
            database.find(query(Execution.class, eq("beanName", executeServiceName), eq("success", true))).isEmpty();

        if (noSuccessExecutions) {
          try {
            long startTime = currentTimeMillis();
            logger.info("Processing execution: " + executeServiceName);

            executeService.execute();

            long endTime = currentTimeMillis();
            logger.info("Processed execution: " + executeServiceName + " in "
                            + (1.0 * (endTime - startTime) / 1000) + " seconds");
          } catch (Exception e) {
            // TODO: zapisanie informacji o błędzie wykonania
            logger.error("Error while processing execution: " + executeServiceName, e);
          } finally {
            sessionState.setUser(null);
          }

          database.create(execution);

          transactionManager.commit(transaction);
        }
      } catch (Exception e) {
        logger.error("Error while processing execution: " + executeServiceName, e);
        transactionManager.rollback(transaction);
        transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        execution.setSuccess(false);
        transactionManager.commit(transaction);
      }

    }
  }


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

