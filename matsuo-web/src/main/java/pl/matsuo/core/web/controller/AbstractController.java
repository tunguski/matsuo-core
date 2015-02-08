package pl.matsuo.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;
import pl.matsuo.core.params.IQueryRequestParams;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.IFacadeBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hibernate.criterion.MatchMode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.ReflectUtil.*;
import static pl.matsuo.core.util.StringUtil.*;


@Transactional
public abstract class AbstractController<E extends AbstractEntity, P extends IQueryRequestParams> {


  @Autowired
  protected Database database;
  @Autowired
  protected IFacadeBuilder facadeBuilder;


  @SuppressWarnings("unchecked")
  protected final Class<E> entityType = resolveType(getClass(), AbstractController.class, 0);


  /**
   * Lista pól z którymi należy porównywać wartość parametru 'query' z zapytania listującego
   * elementy.
   */
  protected List<String> queryMatchers() {
    return emptyList();
  }


  /**
   * Tworzy proste zapytanie na podstawie przekazanej mapy parametrów. Jedynym obsługiwanym
   * parametrem jest 'query' - na podstawie listy pól zwracanych przez {@link #queryMatchers()}
   * buduje zapytanie wymagające aby każde słowo z 'query' znalazło się w którymś z pól.
   */
  protected AbstractQuery<E> listQuery(P params, Condition... additionalConditions) {
    List<Condition> conditions = new ArrayList<>(asList(additionalConditions));

    if (notEmpty(params.getQuery())) {
      if (queryMatchers().isEmpty()) {
        throw new IllegalStateException("When using query parameter queryMatchers must be defined");
      }

      String[] parts = params.getQuery().split(" ");

      for (String part : parts) {
        List<Condition> partConditions = new ArrayList<>();

        for (String queryMatcher : queryMatchers()) {
          partConditions.add(ilike(queryMatcher, part.trim(), ANYWHERE));
        }

        conditions.add(or(partConditions.toArray(new Condition[0])));
      }
    }

    return entityQuery(conditions.toArray(new Condition[0]));
  }


  /**
   * Domyślna metoda listująca elementy według zadanych parametrów.
   */
  @RequestMapping(method = GET)
  public List<E> list(P params) {
    AbstractQuery<E> query = listQuery(params);

    if (params.getLimit() != null && params.getLimit() > 0) {
      query.limit(params.getLimit());
    }
    if (params.getOffset() != null && params.getOffset() > 0) {
      query.offset(params.getOffset());
    }

    List<E> list = database.find(query);

    return list;
  }


  /**
   * Pomocnicza metoda wyszukiwania gdy zapytanie wymaga jedynie przekazania kryteriów.
   */
  protected List<E> list(Condition... conditions) {
    return database.find(entityQuery(conditions));
  }


  /**
   * Pomocnicza metoda wyszukiwania gdy zapytanie wymaga jedynie przekazania kryteriów.
   */
  protected AbstractQuery<E> entityQuery(Condition... conditions) {
    return query(entityType, conditions).initializer(entityInitializers);
  }


  /**
   * Pobiera listę encji danego typu po kolekcji identyfikatorów.
   */
  @RequestMapping(value = "/list/byIds", method = GET, consumes = {APPLICATION_OCTET_STREAM_VALUE})
  public List<E> listByIds(@RequestParam("ids") List<Integer> ids) {
    return database.find(entityQuery(in("id", ids)));
  }


  /**
   * Pobiera pojedynczą encję danego typu po id.
   */
  @RequestMapping(value = "/{id}", method = GET)
  public HttpEntity<E> find(@PathVariable("id") Integer id) {
    try {
      return new HttpEntity<E>(database.findById(entityType, id, entityInitializers));
    } catch (IllegalArgumentException e) {
      throw new EntityNotFoundException(id);
    }
  }


  protected final Initializer<E>[] entityInitializers = entityInitializers().toArray(new Initializer[0]);


  protected List<? extends Initializer<? super E>> entityInitializers() {
    return new ArrayList<>();
  }


  @RequestMapping(method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<E> create(@RequestBody @Valid E entity,
                              @Value("#{request.requestURL}") StringBuffer parentUri) {
    entity = database.create(entity);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(childLocation(parentUri, entity.getId()));
    return new HttpEntity<E>(headers);
  }


  @RequestMapping(method = PUT, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void update(@RequestBody @Valid E entity) {
    database.update(entity);
  }


  @RequestMapping(value = "/{id}", method = DELETE)
  @ResponseStatus(NO_CONTENT)
  public void delete(@PathVariable("id") Integer id) {
    database.delete(entityType, id);
  }


  @RequestMapping(value = "/{id}", method = PUT)
  @ResponseStatus(NO_CONTENT)
  public void update(@PathVariable("id") Integer id, @RequestBody E entity) {
    entity.setId(id);
    database.create(entity);
  }


  protected URI childLocation(StringBuffer parentUri, Object childId) {
    UriTemplate uri = new UriTemplate(parentUri.append("/{childId}").toString());
    return uri.expand(childId);
  }


  protected <E> HttpEntity<E> httpEntity(AbstractEntity entity, StringBuffer parentUri) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(childLocation(parentUri, entity.getId()));
    return new HttpEntity<E>(headers);
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

