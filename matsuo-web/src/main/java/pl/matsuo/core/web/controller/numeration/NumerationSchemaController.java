package pl.matsuo.core.web.controller.numeration;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.numeration.NumerationSchema;
import pl.matsuo.core.web.controller.AbstractController;
import pl.matsuo.core.web.controller.params.IEntityQueryRequestParams;

@RestController
@RequestMapping("/numerationSchemas")
public class NumerationSchemaController
    extends AbstractController<NumerationSchema, IEntityQueryRequestParams> {

  @RequestMapping(
      method = POST,
      consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  @Override
  public HttpEntity<NumerationSchema> create(
      @RequestBody @Valid NumerationSchema entity,
      @Value("#{request.requestURL}") StringBuffer parentUri) {
    if (entity.getValue() == null) {
      entity.setValue(entity.getMinValue());
    }
    return super.create(entity, parentUri);
  }
}
