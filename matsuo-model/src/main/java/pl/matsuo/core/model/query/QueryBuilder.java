package pl.matsuo.core.model.query;

import com.google.common.base.Joiner;
import org.hibernate.criterion.MatchMode;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.query.condition.AbstractQueryFunction;
import pl.matsuo.core.model.query.condition.ComplexCondition;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.query.condition.FromPart;
import pl.matsuo.core.model.query.condition.LeftJoinElement;
import pl.matsuo.core.model.query.condition.QueryFunction;
import pl.matsuo.core.model.query.condition.QueryPart;
import pl.matsuo.core.model.query.condition.SelectPart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.*;
import static org.hibernate.criterion.MatchMode.*;


public class QueryBuilder {


  public static <E extends AbstractEntity> AbstractQuery<E> query(Class<E> entityClass, QueryPart ... queryParts) {
    AbstractQuery<E> query = new AbstractQuery<E>(entityClass);
    query.parts(queryParts);
    return query;
  }


  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em. Jeśli to napis, to nie może być pusty.
   */
  public static Condition maybe(final Object shouldApply, final Condition condition) {
    return maybe(shouldApply != null
        && (!(shouldApply instanceof String) || !((String)shouldApply).isEmpty()), condition);
  }


  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em. Jeśli to napis, to nie może być pusty.
   */
  public static Condition maybeEq(final Object value, final String propertyName) {
    return maybe(value, eq(propertyName, value));
  }


  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em.
   */
  public static Condition maybe(final boolean apply, final Condition condition) {
    if (apply) {
      return condition;
    } else {
      return null;
    }
  }


  public static SelectPart select(String ... fields) {
    return new SelectPart(fields);
  }


  protected static Condition operator(String fieldName, String operator, Object value) {
    return query -> value == null ? "1 = 1" : fieldName + " " + operator + " " + query.propertyValue(value);
  }


  public static Condition eq(String fieldName, Object value) {
    return operator(fieldName, "=", value);
  }


  public static Condition ne(String fieldName, Object value) {
    return operator(fieldName, "!=", value);
  }


  public static Condition gt(String fieldName, Object value) {
    return operator(fieldName, ">", value);
  }


  public static Condition ge(String fieldName, Object value) {
    return operator(fieldName, ">=", value);
  }


  public static Condition lt(String fieldName, Object value) {
    return operator(fieldName, "<", value);
  }


  public static Condition le(String fieldName, Object value) {
    return operator(fieldName, "<=", value);
  }


  public static Condition ilike(String fieldName, Object value) {
    return ilike(fieldName, value, ANYWHERE);
  }


  public static Condition ilike(String fieldName, Object value, MatchMode matchMode) {
    return operator("lower(" + fieldName + ")", "like", matchMode.toMatchString(value.toString().toLowerCase()));
  }


  public static <E extends Collection<?>> Condition in(final String fieldName, final E value) {
    return query -> {
      List<String> properties = new ArrayList<>();
      for (Object o : value) {
        if (o != null) {
          properties.add(query.propertyValue(o));
        }
      }

      return fieldName + " in (" + Joiner.on(", ").join(properties) + ")";
    };
  }


  public static Condition in(String fieldName, Object[] values) {
    return in(fieldName, asList(values));
  }


  public static Condition in(String fieldName, AbstractQuery query) {
    return in(fieldName, asList(query));
  }


  public static Condition between(final String fieldName, final Object min, final Object max) {
    return query -> fieldName + " between " + query.propertyValue(min) + " AND " + query.propertyValue(max);
  }


  public static Condition isNull(final String fieldName) {
    return query -> fieldName + " IS NULL";
  }


  public static Condition isNotNull(final String fieldName) {
    return query -> fieldName + " IS NOT NULL";
  }


  public static Condition eqOrIsNull(String fieldName, Object value) {
    return or(eq(fieldName, value), isNull(fieldName));
  }


  public static Condition or(final Condition ... conditions) {
    return new ComplexCondition("OR", conditions);
  }


  public static Condition and(final Condition ... conditions) {
    return new ComplexCondition("AND", conditions);
  }


  public static Condition not(final Condition condition) {
    return query -> "NOT " + condition.print(query);
  }


  public static Condition cond(final String condition) {
    return query -> "(" + condition + ")";
  }


  public static Condition memberOf(String fieldName, Object value) {
    return operator(fieldName, "MEMBER OF", value);
  }


  public static QueryFunction max(String fieldName) {
    return new AbstractQueryFunction(fieldName, "max");
  }


  public static QueryFunction min(String fieldName) {
    return new AbstractQueryFunction(fieldName, "min");
  }


  public static QueryFunction avg(String fieldName) {
    return new AbstractQueryFunction(fieldName, "avg");
  }


  /**
   * Aby obecnie działało, to warunek musi obsługiwać nulle
   */
  public static FromPart leftJoin(String alias, Class clazz, Condition joinCondition) {
    return new LeftJoinElement(alias, clazz, joinCondition);
  }


  public static FromPart join(String alias, String joinPath) {
    return new FromPart("", alias, joinPath);
  }


}

