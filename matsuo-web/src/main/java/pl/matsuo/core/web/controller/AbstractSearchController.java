package pl.matsuo.core.web.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hibernate.criterion.MatchMode.ANYWHERE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.matsuo.core.model.query.QueryBuilder.ilike;
import static pl.matsuo.core.model.query.QueryBuilder.in;
import static pl.matsuo.core.model.query.QueryBuilder.or;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.util.ReflectUtil.resolveType;
import static pl.matsuo.core.util.StringUtil.notEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.IFacadeBuilder;

@Transactional
public abstract class AbstractSearchController<
    E extends AbstractEntity, P extends IQueryRequestParams> {

  @Autowired protected Database database;
  @Autowired protected IFacadeBuilder facadeBuilder;

  @SuppressWarnings("unchecked")
  protected final Class<E> entityType = resolveType(getClass(), AbstractSearchController.class, 0);

  /**
   * Lista pól z którymi należy porównywać wartość parametru 'query' z zapytania listującego
   * elementy.
   */
  protected <F extends AbstractEntity> List<Function<F, String>> queryMatchers(Class<F> entity) {
    if (entity.equals(entityType)) {
      return (List) queryMatchers();
    }

    throw new RuntimeException("Custom entity type requires queryMatchers(Class) redefinition");
  }

  /**
   * Lista pól z którymi należy porównywać wartość parametru 'query' z zapytania listującego
   * elementy.
   */
  protected List<Function<E, String>> queryMatchers() {
    return emptyList();
  }

  /**
   * Tworzy proste zapytanie na podstawie przekazanej mapy parametrów. Jedynym obsługiwanym
   * parametrem jest 'query' - na podstawie listy pól zwracanych przez {@link #queryMatchers(Class)}
   * buduje zapytanie wymagające aby każde słowo z 'query' znalazło się w którymś z pól.
   */
  protected <F extends AbstractEntity> AbstractQuery<F> listQuery(
      Class<F> entity,
      P params,
      List<Function<F, String>> queryMatchers,
      Condition... additionalConditions) {
    List<Condition> conditions = new ArrayList<>(asList(additionalConditions));

    if (notEmpty(params.getQuery())) {
      if (queryMatchers.isEmpty()) {
        throw new IllegalStateException("When using query parameter queryMatchers must be defined");
      }

      String[] parts = params.getQuery().split(" ");

      for (String part : parts) {
        List<Condition> partConditions = new ArrayList<>();

        for (Function<F, String> queryMatcher : queryMatchers) {
          partConditions.add(ilike(queryMatcher, part.trim(), ANYWHERE));
        }

        conditions.add(or(partConditions.toArray(new Condition[0])));
      }
    }

    return entityQuery(entity, conditions.toArray(new Condition[0]));
  }

  protected <F extends AbstractEntity> AbstractQuery<F> listQuery(
      Class<F> entity, P params, Condition... additionalConditions) {
    return listQuery(entity, params, queryMatchers(entity), additionalConditions);
  }

  protected AbstractQuery<E> listQuery(P params, Condition... additionalConditions) {
    return listQuery(entityType, params, queryMatchers(), additionalConditions);
  }

  /** Domyślna metoda listująca elementy według zadanych parametrów. */
  protected <F extends AbstractEntity> List<F> list(Class<F> entity, P params) {
    AbstractQuery<F> query = listQuery(entity, params);

    if (params.getLimit() != null && params.getLimit() > 0) {
      query.limit(params.getLimit());
    }
    if (params.getOffset() != null && params.getOffset() > 0) {
      query.offset(params.getOffset());
    }

    List<F> list = database.find(query);

    return list;
  }

  /** Domyślna metoda listująca elementy według zadanych parametrów. */
  @RequestMapping(method = GET)
  public List<E> list(P params) {
    return list(entityType, params);
  }

  /** Pomocnicza metoda wyszukiwania gdy zapytanie wymaga jedynie przekazania kryteriów. */
  protected List<E> list(Condition... conditions) {
    return database.find(entityQuery(entityType, conditions));
  }

  /** Pomocnicza metoda wyszukiwania gdy zapytanie wymaga jedynie przekazania kryteriów. */
  protected <F extends AbstractEntity> AbstractQuery<F> entityQuery(
      Class<F> entity, Condition... conditions) {
    return query(entity, conditions).initializer(entityInitializers);
  }

  /** Pobiera listę encji danego typu po kolekcji identyfikatorów. */
  @RequestMapping(
      value = "/list/byIds",
      method = GET,
      consumes = {APPLICATION_OCTET_STREAM_VALUE})
  public List<E> listByIds(@RequestParam("ids") List<Integer> ids) {
    return database.find(entityQuery(entityType, in(AbstractEntity::getId, ids)));
  }

  /** Pobiera pojedynczą encję danego typu po id. */
  @RequestMapping(value = "/{id}", method = GET)
  public HttpEntity<E> find(@PathVariable("id") Integer id) {
    try {
      return new HttpEntity<E>(database.findById(entityType, id, entityInitializers));
    } catch (IllegalArgumentException e) {
      throw new EntityNotFoundException(id);
    }
  }

  protected final Initializer<E>[] entityInitializers =
      entityInitializers().toArray(new Initializer[0]);

  protected List<? extends Initializer<? super E>> entityInitializers() {
    return new ArrayList<>();
  }

  @ResponseStatus(NOT_FOUND)
  public static class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Integer id) {
      super("Entity '" + id + "' not found.");
    }
  }

  public void setDatabase(Database database) {
    this.database = database;
  }

  public void setFacadeBuilder(IFacadeBuilder facadeBuilder) {
    this.facadeBuilder = facadeBuilder;
  }
}
