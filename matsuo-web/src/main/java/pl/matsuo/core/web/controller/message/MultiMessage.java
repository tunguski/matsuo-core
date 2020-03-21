package pl.matsuo.core.web.controller.message;

import java.util.List;
import javax.validation.Valid;
import pl.matsuo.core.model.message.AbstractMessage;
import pl.matsuo.core.web.controller.organization.SimpleParty;

/** Created by tunguski on 20.12.13. */
public class MultiMessage<E extends AbstractMessage> {

  @Valid private E message;
  private List<SimpleParty> parties;

  public E getMessage() {
    return message;
  }

  public void setMessage(E smsMessage) {
    this.message = smsMessage;
  }

  public List<SimpleParty> getParties() {
    return parties;
  }

  public void setParties(List<SimpleParty> parties) {
    this.parties = parties;
  }
}
