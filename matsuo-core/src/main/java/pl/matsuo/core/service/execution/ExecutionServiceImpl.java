package pl.matsuo.core.service.execution;

import static java.lang.System.currentTimeMillis;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.query;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

/** Execute defined (mostly database) changes. */
@Slf4j
@Service
public class ExecutionServiceImpl
    implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

  @Autowired(required = false)
  List<IExecuteService> executeServices;

  @Autowired Database database;
  @Autowired PlatformTransactionManager transactionManager;
  @Autowired SessionState sessionState;
  ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!event.getApplicationContext().equals(applicationContext)
        || executeServices == null
        || executeServices.isEmpty()) {
      return;
    }

    Collections.sort(
        executeServices,
        new AnnotationAwareOrderComparator() {
          @Override
          protected int getOrder(Object obj) {
            int order = super.getOrder(obj);

            return order == LOWEST_PRECEDENCE ? HIGHEST_PRECEDENCE : order;
          }
        });

    for (IExecuteService executeService : executeServices) {
      String executeServiceName = executeService.getExecuteServiceName();

      TransactionStatus transaction =
          transactionManager.getTransaction(new DefaultTransactionDefinition());

      Execution execution = new Execution();
      execution.setBeanName(executeServiceName);
      execution.setRunDate(new Date());
      execution.setSuccess(true);

      try {
        boolean noSuccessExecutions =
            database
                .find(
                    query(
                        Execution.class,
                        eq(Execution::getBeanName, executeServiceName),
                        eq(Execution::getSuccess, true)))
                .isEmpty();

        if (noSuccessExecutions) {
          try {
            long startTime = currentTimeMillis();
            log.info("Processing execution: " + executeServiceName);

            executeService.execute();

            long endTime = currentTimeMillis();
            log.info(
                "Processed execution: "
                    + executeServiceName
                    + " in "
                    + (1.0 * (endTime - startTime) / 1000)
                    + " seconds");
          } catch (Exception e) {
            // TODO: save information about unsuccessful script execution
            log.error("Error while processing execution: " + executeServiceName, e);
          } finally {
            sessionState.setUser(null);
          }

          database.create(execution);

          transactionManager.commit(transaction);
        }
      } catch (Exception e) {
        log.error("Error while processing execution: " + executeServiceName, e);
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
