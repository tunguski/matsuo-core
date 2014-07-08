package pl.matsuo.core.web.controller.numeration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.numeration.Numeration;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.web.controller.AbstractController;
import pl.matsuo.core.web.controller.params.IEntityQueryRequestParams;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 15.09.13.
 */
@RestController
@RequestMapping("/numerations")
public class NumerationController extends AbstractController<Numeration, IEntityQueryRequestParams> {


  @Override
  protected AbstractQuery<Numeration> listQuery(IEntityQueryRequestParams params, Condition... conditions) {
    return super.listQuery(params, maybeEq(params.getIdEntity(), "idEntity"));
  }


  @RequestMapping(method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  @Override
  public HttpEntity<Numeration> create(@RequestBody @Valid Numeration entity,
                                       @Value("#{request.requestURL}") StringBuffer parentUri) {
    if (entity.getValue() == null) {
      entity.setValue(entity.getMinValue());
    }
    return super.create(entity, parentUri);
  }
}

