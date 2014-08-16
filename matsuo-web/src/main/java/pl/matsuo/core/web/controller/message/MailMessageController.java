package pl.matsuo.core.web.controller.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.message.MailMessage;


/**
 * Created by marek on 16.08.14.
 */
@RestController
@RequestMapping("/mailMessages")
public class MailMessageController extends AbstractMessageController<MailMessage> {


  @Override
  protected MailMessage copyMessage(MailMessage message) {
    return null;
  }
}

