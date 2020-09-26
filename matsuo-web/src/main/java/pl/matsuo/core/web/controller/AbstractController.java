package pl.matsuo.core.web.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.params.IQueryRequestParams;

@Transactional
public abstract class AbstractController<E extends AbstractEntity, P extends IQueryRequestParams>
    extends AbstractSearchController<E, P> {

  @RequestMapping(
      method = POST,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<E> create(
      @RequestBody @Valid E entity, @Value("#{request.requestURL}") StringBuffer parentUri) {
    entity = database.create(entity);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(childLocation(parentUri, entity.getId()));
    return new HttpEntity<E>(headers);
  }

  @RequestMapping(
      method = PUT,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void update(@RequestBody @Valid E entity) {
    database.update(entity);
  }

  @RequestMapping(value = "/{id}", method = DELETE)
  @ResponseStatus(NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    database.delete(entityType, id);
  }

  @RequestMapping(value = "/{id}", method = PUT)
  @ResponseStatus(NO_CONTENT)
  public void update(@PathVariable("id") Long id, @RequestBody E entity) {
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
}
