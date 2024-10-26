package pl.matsuo.core.web.controller.message;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.message.AbstractMessage;
import pl.matsuo.core.web.controller.organization.SimpleParty;

@Getter
@Setter
public class MultiMessage<E extends AbstractMessage> {

  @Valid private E message;
  private List<SimpleParty> parties;
}
