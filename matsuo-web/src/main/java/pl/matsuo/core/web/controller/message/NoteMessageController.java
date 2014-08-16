package pl.matsuo.core.web.controller.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.message.NoteMessage;


/**
 * Created by marek on 16.08.14.
 */
@RestController
@RequestMapping("/notes")
public class NoteMessageController extends AbstractMessageController<NoteMessage> {


  @Override
  protected NoteMessage copyMessage(NoteMessage message) {
    return null;
  }
}

