package pl.matsuo.core.web.controller.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.core.model.message.SmsMessage;

@RestController
@RequestMapping("/smsMessages")
public class SmsMessageController extends AbstractMessageController<SmsMessage> {

  @Override
  protected SmsMessage copyMessage(SmsMessage message) {
    return message;
  }
}
