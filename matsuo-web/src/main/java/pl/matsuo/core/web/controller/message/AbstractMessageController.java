package pl.matsuo.core.web.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.matsuo.core.model.message.AbstractMessage;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.query.AbstractQuery;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.user.Group;
import pl.matsuo.core.service.user.IGroupsService;
import pl.matsuo.core.web.controller.AbstractController;
import pl.matsuo.core.web.controller.organization.SimpleParty;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 21.12.13.
 */
public abstract class AbstractMessageController<E extends AbstractMessage> extends AbstractController<E, IMessageRequestParams> {


  @Autowired
  protected IGroupsService groupsService;


  @RequestMapping(value = "/multiMessage", method = POST, consumes = { APPLICATION_JSON_VALUE })
  @ResponseStatus(CREATED)
  public void create(@RequestBody @Valid MultiMessage<E> entity,
                     @Value("#{request.requestURL}") StringBuffer parentUri) {
    for (SimpleParty simpleParty : entity.getParties()) {
      E smsMessage = copyMessage(entity.getMessage());

      if (AbstractParty.class.isAssignableFrom(simpleParty.getType())) {
        smsMessage.setIdParty(simpleParty.getId());
        database.create(smsMessage);
      } else if (Group.class.isAssignableFrom(simpleParty.getType())) {
        // TODO: wyszukiwanie podmiotów poprzed definicję grupy
        throw new RuntimeException("Not implemented yet");
      }
    }
  }


  protected abstract E copyMessage(E message);


  @Override
  protected AbstractQuery<E> listQuery(IMessageRequestParams params, Condition... additionalConditions) {
    return super.listQuery(params, additionalConditions).parts(eq("idParty", params.getIdParty()));
  }
}

