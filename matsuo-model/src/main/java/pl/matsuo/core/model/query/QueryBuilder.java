package pl.matsuo.core.model.query;

import static java.util.Arrays.*;
import static org.hibernate.criterion.MatchMode.*;
import static org.mockito.Mockito.*;
import static org.springframework.util.StringUtils.*;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.hibernate.criterion.MatchMode;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.query.condition.AbstractQueryFunction;
import pl.matsuo.core.model.query.condition.ComplexCondition;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.query.condition.FieldCondition;
import pl.matsuo.core.model.query.condition.FromPart;
import pl.matsuo.core.model.query.condition.LeftJoinElement;
import pl.matsuo.core.model.query.condition.QueryFunction;
import pl.matsuo.core.model.query.condition.QueryPart;
import pl.matsuo.core.model.query.condition.SelectPart;

public class QueryBuilder {

  public static <E extends AbstractEntity> AbstractQuery<E> query(
      Class<E> entityClass, QueryPart<E>... queryParts) {
    AbstractQuery<E> query = new AbstractQuery<>(entityClass);
    query.parts(queryParts);
    return query;
  }

  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em. Jeśli to napis, to nie może być pusty.
   */
  public static <T extends AbstractEntity> Condition<T> maybe(
      final Object shouldApply, final Condition<T> condition) {
    return maybe(
        shouldApply != null
            && (!(shouldApply instanceof String) || !((String) shouldApply).isEmpty()),
        condition);
  }

  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em. Jeśli to napis, to nie może być pusty.
   */
  public static <T extends AbstractEntity, R> FieldCondition<T, R> maybeEq(
      final R value, final Function<T, R> getter) {
    return value != null ? query -> maybe(value, eq(getter, value)).print(query) : null;
  }

  /**
   * Tworzy warunek, który jest dodawany wyłącznie gdy obiekt <code>shouldApply</code> nie jest
   * <code>null</code>em.
   */
  public static <T extends AbstractEntity> Condition<T> maybe(
      final boolean apply, final Condition<T> condition) {
    return apply ? condition : null;
  }

  public static SelectPart select(String... fields) {
    return new SelectPart(fields);
  }

  protected static <T extends AbstractEntity, R> FieldCondition<T, R> operator(
      Function<T, R> getter, String operator, R value) {
    return query ->
        value == null
            ? "1 = 1"
            : query.resolveFieldName(getter) + " " + operator + " " + query.propertyValue(value);
  }

  protected static <T extends AbstractEntity, R> FieldCondition<T, R> textOperator(
      String text, String operator, R value) {
    return query ->
        value == null ? "1 = 1" : text + " " + operator + " " + query.propertyValue(value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> eq(
      Function<T, R> getter, R value) {
    return query -> operator(getter, "=", value).print(query);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> ne(
      Function<T, R> getter, R value) {
    return operator(getter, "!=", value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> gt(
      Function<T, R> getter, R value) {
    return operator(getter, ">", value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> ge(
      Function<T, R> getter, R value) {
    return operator(getter, ">=", value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> lt(
      Function<T, R> getter, R value) {
    return operator(getter, "<", value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> le(
      Function<T, R> getter, R value) {
    return operator(getter, "<=", value);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> ilike(
      Function<T, R> getter, R value) {
    return ilike(getter, value, ANYWHERE);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> ilike(
      Function<T, R> getter, R value, MatchMode matchMode) {
    return query ->
        textOperator(
                "lower(" + query.resolveFieldName(getter) + ")",
                "like",
                matchMode.toMatchString(value.toString().toLowerCase()))
            .print((AbstractQuery<AbstractEntity>) query);
  }

  public static <E extends Collection<R>, T extends AbstractEntity, R> FieldCondition<T, R> in(
      final Function<T, R> getter, final E value) {
    return query -> {
      List<String> properties = new ArrayList<>();
      for (Object o : value) {
        if (o != null) {
          properties.add(query.propertyValue(o));
        }
      }

      return query.resolveFieldName(getter) + " in (" + Joiner.on(", ").join(properties) + ")";
    };
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> in(
      Function<T, R> getter, R[] values) {
    return in(getter, asList(values));
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> in(
      Function<T, R> getter, AbstractQuery query) {
    return in(getter, (List<R>) asList(query));
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> between(
      final Function<T, R> getter, final R min, final R max) {
    return query ->
        query.resolveFieldName(getter)
            + " BETWEEN "
            + query.propertyValue(min)
            + " AND "
            + query.propertyValue(max);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> isNull(
      final Function<T, R> getter) {
    return query -> query.resolveFieldName(getter) + " IS NULL";
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> isNotNull(
      final Function<T, R> getter) {
    return query -> query.resolveFieldName(getter) + " IS NOT NULL";
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> eqOrIsNull(
      Function<T, R> getter, R value) {
    return query -> or(eq(getter, value), isNull(getter)).print(query);
  }

  public static <T extends AbstractEntity> Condition<T> or(final Condition<T>... conditions) {
    return new ComplexCondition("OR", conditions);
  }

  public static <T extends AbstractEntity> Condition<T> and(final Condition<T>... conditions) {
    return new ComplexCondition("AND", conditions);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> not(
      final Condition<T> condition) {
    return query -> "NOT " + condition.print(query);
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> cond(final String condition) {
    return query -> "(" + condition + ")";
  }

  public static <T extends AbstractEntity, R> FieldCondition<T, R> memberOf(
      Function<T, R> getter, R value) {
    return operator(getter, "MEMBER OF", value);
  }

  public static <T extends AbstractEntity, R> QueryFunction<T> max(Function<T, R> getter) {
    return new AbstractQueryFunction(getter, "max");
  }

  public static <T extends AbstractEntity, R> QueryFunction<T> min(Function<T, R> getter) {
    return new AbstractQueryFunction(getter, "min");
  }

  public static <T extends AbstractEntity, R> QueryFunction<T> avg(Function<T, R> getter) {
    return new AbstractQueryFunction(getter, "avg");
  }

  /** Aby obecnie działało, to warunek musi obsługiwać nulle */
  public static <X extends AbstractEntity> FromPart leftJoin(
      String alias, Class<X> clazz, Condition joinCondition) {
    return new LeftJoinElement(alias, clazz, joinCondition);
  }

  public static FromPart join(String alias, String joinPath) {
    return new FromPart("JOIN", alias, joinPath);
  }

  public static <T extends AbstractEntity, K extends AbstractEntity, R> Function<T, R> sub(
      Function<T, K> getter, Function<K, R> subGetter) {
    return t -> subGetter.apply(getter.apply(t));
  }

  public static <T extends AbstractEntity, K extends AbstractEntity, R>
      Function<T, R> subCollection(Function<T, Collection<K>> getter, Function<K, R> subGetter) {
    return t -> subGetter.apply(getter.apply(t).iterator().next());
  }

  public static <T extends AbstractEntity, K extends AbstractEntity, S extends AbstractEntity, R>
      Function<T, R> sub(
          Function<T, K> getter, Function<K, S> subGetter, Function<S, R> subSubGetter) {
    return t -> subSubGetter.apply(subGetter.apply(getter.apply(t)));
  }
}
