package pl.matsuo.core.web.controller.message;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.core.model.message.NoteStatus.*;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.message.NoteMessage;
import pl.matsuo.core.model.message.NoteStatus;

/** Created by marek on 16.08.14. */
@RestController
@RequestMapping("/noteMessages")
public class NoteMessageController extends AbstractMessageController<NoteMessage> {

  @Override
  protected NoteMessage copyMessage(NoteMessage message) {
    return message;
  }

  protected HttpEntity<String> changeAppointmentStatus(Integer id, NoteStatus status) {
    NoteMessage appointment = database.findById(NoteMessage.class, id);
    appointment.setStatus(status);
    database.update(appointment);
    return new HttpEntity<String>(appointment.getStatus().name());
  }

  @RequestMapping(value = "/{id}/close", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> close(@PathVariable("id") Integer id) {
    return changeAppointmentStatus(id, CLOSED);
  }

  @RequestMapping(value = "/{id}/cancel", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> cancel(@PathVariable("id") Integer id) {
    return changeAppointmentStatus(id, CANCELLED);
  }

  @RequestMapping(value = "/{id}/open", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> open(@PathVariable("id") Integer id) {
    return changeAppointmentStatus(id, OPEN);
  }
}
