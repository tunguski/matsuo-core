package pl.matsuo.core.model.query;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.springframework.util.StringUtils.uncapitalize;
import static pl.matsuo.core.util.collection.CollectionUtil.merge;
import static pl.matsuo.core.util.collection.CollectionUtil.removeNulls;

import com.google.common.base.Joiner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.hibernate.SessionFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.query.condition.FromPart;
import pl.matsuo.core.model.query.condition.QueryFunction;
import pl.matsuo.core.model.query.condition.QueryPart;
import pl.matsuo.core.model.query.condition.SelectPart;
import pl.matsuo.core.util.collection.CollectionUtil;

/**
 * Abstrakcyjna nadklasa dla zapytań.
 *
 * @since Aug 23, 2013
 * @param <E>
 */
public class AbstractQuery<E extends AbstractEntity> implements Query<E> {
  private static final Logger logger = LoggerFactory.getLogger(AbstractQuery.class);

  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

  @Autowired protected SessionFactory sessionFactory;

  private List<SelectPart> select = new ArrayList<>();
  private List<FromPart> from = new ArrayList<>();
  private List<Condition> where = new ArrayList<>();
  private List<String> groupBy = new ArrayList<>();
  private List<Condition> having = new ArrayList<>();
  private List<String> orderBy = new ArrayList<>();
  protected List<Initializer<? super E>> initializers = new ArrayList<>();
  private Integer limit;
  private Integer offset;
  private Class<E> clazz;

  private ThreadLocal<String> methodName = new ThreadLocal<>();
  private final E mock;

  public AbstractQuery(Class<E> clazz) {
    Assert.notNull(clazz);

    this.clazz = clazz;
    mock =
        mock(
            clazz,
            new Answer() {

              private Object innerMock(InvocationOnMock base) {
                return mock(
                    base.getMethod().getReturnType(),
                    (Answer)
                        invocation -> {
                          if (invocation.getMethod().getName().startsWith("get")) {
                            methodName.set(
                                methodName.get()
                                    + "."
                                    + uncapitalize(invocation.getMethod().getName().substring(3)));
                          }

                          if (AbstractEntity.class.isAssignableFrom(
                              invocation.getMethod().getReturnType())) {
                            return innerMock(invocation);
                          }

                          return null;
                        });
              }

              @Override
              public Object answer(InvocationOnMock invocation) throws Throwable {
                if (invocation.getMethod().getName().startsWith("get")) {
                  methodName.set(uncapitalize(invocation.getMethod().getName().substring(3)));
                }

                if (AbstractEntity.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
                  return innerMock(invocation);
                }

                return null;
              }
            });

    from.add(new FromPart("", uncapitalize(clazz.getSimpleName()), clazz.getName()));
  }

  public String resolveFieldName(Function<E, ?> getter) {
    getter.apply(mock);
    return methodName.get();
  }

  public AbstractQuery<E> select(String name) {
    select.add(new SelectPart(name));
    return this;
  }

  protected AbstractQuery<E> select(SelectPart selectPart) {
    select.add(selectPart);
    return this;
  }

  protected AbstractQuery<E> from(FromPart fromPart) {
    from.add(fromPart);
    return this;
  }

  public AbstractQuery<E> condition(Condition... conditions) {
    for (Condition condition : conditions) {
      if (condition != null) {
        where.add(condition);
      }
    }

    return this;
  }

  public AbstractQuery<E> parts(QueryPart... queryParts) {
    for (QueryPart queryPart : queryParts) {
      if (queryPart == null) {
        continue;
      } else if (Condition.class.isAssignableFrom(queryPart.getClass())) {
        condition((Condition) queryPart);
      } else if (FromPart.class.isAssignableFrom(queryPart.getClass())) {
        from((FromPart) queryPart);
      } else if (SelectPart.class.isAssignableFrom(queryPart.getClass())) {
        select((SelectPart) queryPart);
      } else {
        throw new RuntimeException("Not implemented yet for " + queryPart.getClass().getName());
      }
    }

    return this;
  }

  public AbstractQuery<E> groupBy(String fieldName) {
    groupBy.add(fieldName);
    return this;
  }

  public AbstractQuery<E> having(Condition condition) {
    having.add(condition);
    return this;
  }

  public AbstractQuery<E> orderBy(String fieldName) {
    orderBy.add(fieldName);
    return this;
  }

  public AbstractQuery<E> limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public AbstractQuery<E> offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public AbstractQuery<E> initializer(Initializer<? super E>... initializer) {
    this.initializers.addAll(asList(initializer));
    return this;
  }

  public String propertyValue(Object value) {
    if (Number.class.isAssignableFrom(value.getClass())
        || Boolean.class.isAssignableFrom(value.getClass())) {
      return value.toString();
    } else if (Date.class.isAssignableFrom(value.getClass())) {
      return "'" + dateFormat.format(value) + "'";
    } else if (QueryFunction.class.isAssignableFrom(value.getClass())) {
      return ((QueryFunction) value).print(this);
    } else if (AbstractQuery.class.isAssignableFrom(value.getClass())) {
      return ((AbstractQuery) value).printQuery();
    } else if (Class.class.isAssignableFrom(value.getClass())) {
      return "'" + ((Class) value).getName() + "'";
    } else {
      return "'" + value + "'";
    }
  }

  protected ThreadLocal<Integer> idBucket = new ThreadLocal();

  @Override
  public List<E> query(Integer idBucketValue) {
    try {
      idBucket.set(idBucketValue);
      String queryString = printQuery();

      // wyszukaj w bazie danych
      try {
        org.hibernate.query.Query query =
            sessionFactory.getCurrentSession().createQuery(queryString);
        if (limit != null && limit >= 0) {
          query.setMaxResults(limit);
        }
        if (offset != null && offset >= 0) {
          query.setFirstResult(offset);
        }
        List<E> result = query.list();

        for (E element : result) {
          for (Initializer<? super E> initializer : initializers) {
            initializer.init(element);
          }
        }

        return result;
      } catch (RuntimeException e) {
        logger.error("Error in query: " + queryString);
        throw e;
      }
    } finally {
      idBucket.set(null);
    }
  }

  /** Tworzy tekstową reprezentację danego zapytania. */
  public String printQuery() {
    StringBuilder sb = new StringBuilder();

    // select ...
    if (!select.isEmpty()) {
      sb.append("SELECT ");

      for (SelectPart selectPart : select) {
        sb.append(selectPart.print(this).trim() + ", ");
      }

      sb.delete(sb.length() - 2, sb.length());
    }

    // from ...
    sb.append(" FROM");

    for (FromPart fromPart : from) {
      sb.append(" " + fromPart.print(this).trim());
    }

    List<Condition> conditions =
        removeNulls(merge(CollectionUtil.<Condition>collect(from, "joinCondition"), where));

    boolean hasWhere = false;

    // where ...
    if (!conditions.isEmpty()) {
      sb.append(" WHERE ");

      for (Condition condition : conditions) {
        sb.append(condition.print(this).trim() + " AND ");
      }

      sb.delete(sb.length() - 5, sb.length());

      hasWhere = true;
    }

    if (idBucket.get() != null) {
      sb.append(hasWhere ? " AND " : " WHERE ");
      sb.append("idBucket = " + idBucket.get());
    }

    // group by ...
    if (!groupBy.isEmpty()) {
      sb.append(" GROUP BY " + Joiner.on(", ").join(groupBy));
    }

    // having ...
    if (!having.isEmpty()) {
      sb.append(" HAVING ");

      for (Condition condition : having) {
        sb.append(condition.print(this) + " AND ");
      }

      sb.delete(sb.length() - 5, sb.length());
    }

    // order by ...
    if (!orderBy.isEmpty()) {
      sb.append(" ORDER BY " + Joiner.on(", ").join(orderBy));
    }

    return sb.toString().trim();
  }
}
