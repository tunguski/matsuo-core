package pl.matsuo.core.web.controller.message;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.matsuo.core.model.message.NoteStatus.CANCELLED;
import static pl.matsuo.core.model.message.NoteStatus.CLOSED;
import static pl.matsuo.core.model.message.NoteStatus.OPEN;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.message.NoteMessage;
import pl.matsuo.core.model.message.NoteStatus;

@RestController
@RequestMapping("/noteMessages")
public class NoteMessageController extends AbstractMessageController<NoteMessage> {

  @Override
  protected NoteMessage copyMessage(NoteMessage message) {
    return message;
  }

  protected HttpEntity<String> changeAppointmentStatus(Long id, NoteStatus status) {
    NoteMessage appointment = database.findById(NoteMessage.class, id);
    appointment.setStatus(status);
    database.update(appointment);
    return new HttpEntity<String>(appointment.getStatus().name());
  }

  @RequestMapping(value = "/{id}/close", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> close(@PathVariable("id") Long id) {
    return changeAppointmentStatus(id, CLOSED);
  }

  @RequestMapping(value = "/{id}/cancel", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> cancel(@PathVariable("id") Long id) {
    return changeAppointmentStatus(id, CANCELLED);
  }

  @RequestMapping(value = "/{id}/open", method = POST)
  @ResponseStatus(CREATED)
  public HttpEntity<String> open(@PathVariable("id") Long id) {
    return changeAppointmentStatus(id, OPEN);
  }
}
