package pl.matsuo.core.model.message;

import static pl.matsuo.core.model.message.NoteStatus.OPEN;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NoteMessage extends AbstractMessage {

  private NotePriority priority;
  private NoteStatus status = OPEN;
}
